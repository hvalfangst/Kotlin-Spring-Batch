package hvalfangst.batch.config

import hvalfangst.batch.db.SummaryRowMapper
import hvalfangst.batch.listener.JobNotificationListener
import hvalfangst.batch.model.SummaryRecord
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
class BatchConfig(@Qualifier("sourceDatabase") private val sourceDatabase: DataSource) {

    @Bean
    fun job(
        listener: JobNotificationListener,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Job {
        return JobBuilder("DatabaseMigrationJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(salesSummaryStep(jobRepository, transactionManager))
            .end()
            .build()
    }

    @Bean
    fun salesSummaryStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("salesSummaryStep", jobRepository)
            .chunk<SummaryRecord, SummaryRecord>(10, transactionManager)
            .reader(salesReader())
            .processor(salesProcessor())
            .writer(salesWriter())
            .build()
    }

    @Bean
    fun salesReader(): JdbcCursorItemReader<SummaryRecord> {
        return JdbcCursorItemReader<SummaryRecord>().apply {
            dataSource = sourceDatabase
            sql = "SELECT SUM(quantity * price) AS total_sales FROM sales"
            setRowMapper(SummaryRowMapper())
        }
    }

    @Bean
    fun salesProcessor(): ItemProcessor<SummaryRecord, SummaryRecord> {
        // Implement the logic to process the sales summary records here.
        // For example, you might want to perform additional calculations or data transformations.

        return ItemProcessor { summaryRecord ->
            // Your processing logic here.
            // In this example, we don't perform any additional processing.
            summaryRecord
        }
    }

        @Bean
        fun salesWriter(): ItemWriter<SummaryRecord> {
            return ItemWriter { summaryRecords ->
                // Implement the logic to write the processed sales summary records to the target database.
                // You can use the SummaryRecord to store data in the target database.

                // Example: In this placeholder implementation, we simply print the summary records.
                for (summaryRecord in summaryRecords) {
                    println("Writing summary record: $summaryRecord")
                }
            }
        }

    @Qualifier("batchDataSource")
    @Bean
    fun batchDataSource(): DataSource =
        EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("/org/springframework/batch/core/schema-h2.sql")
            .generateUniqueName(true)
            .build()

    @Bean
    fun batchTransactionManager(@Qualifier("batchDataSource") dataSource: DataSource): JdbcTransactionManager =
        JdbcTransactionManager(dataSource)


    @Bean
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher,
        jobExplorer: JobExplorer,
        jobRepository: JobRepository
    ): JobLauncherApplicationRunner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
}
