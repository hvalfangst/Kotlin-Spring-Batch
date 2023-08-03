package hvalfangst.etl.config

import hvalfangst.etl.listener.JobNotificationListener
import hvalfangst.etl.model.SalesRecord
import hvalfangst.etl.model.SummaryRecord
import hvalfangst.etl.service.SalesService
import hvalfangst.etl.service.SummaryService
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.util.*
import javax.sql.DataSource

/**
 * Configuration class for the Spring Batch ETL (Extract, Transform, Load) process.
 *
 * This class provides the necessary configurations to set up and run the ETL job using Spring Batch.
 * The ETL job is designed to extract sales data from a source database for a specific target date,
 * perform data aggregation, and then load the aggregated result into a target database.
 *
 * The configuration includes the definition of the main Job and Step used for the ETL process.
 * It also sets up the necessary data source and transaction manager for batch processing.
 *
 * @param salesService The service component responsible for retrieving sales data from the source database.
 * @param summaryService The service component responsible for inserting summary records into the target database.
 */
@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
class BatchConfig(private val salesService: SalesService, private val summaryService: SummaryService) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * Configures the main Job for the ETL process.
     *
     * @param listener The JobNotificationListener to be used for the Job.
     * @param jobRepository The JobRepository for storing job-related metadata.
     * @param transactionManager The PlatformTransactionManager to handle batch transactions.
     * @return The configured Job for the ETL process.
     */
    @Bean
    fun job(
        listener: JobNotificationListener,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Job {
        return JobBuilder("ETL", jobRepository)
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(etl(jobRepository, transactionManager))
            .end()
            .build()
    }

    /**
     * Configures the main Step for the ETL process.
     *
     * @param jobRepository The JobRepository for storing step-related metadata.
     * @param transactionManager The PlatformTransactionManager to handle step-level transactions.
     * @return The configured Step for the ETL process.
     */
    @Bean
    fun etl(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("etl", jobRepository)
            .tasklet(etlTasklet(), transactionManager)
            .build()
    }

    /**
     * Defines the Tasklet for the ETL process.
     * This Tasklet is responsible for executing the data extraction, transformation, and loading operations.
     *
     * @return The configured Tasklet for the ETL process.
     */
    @Bean
    fun etlTasklet(): Tasklet {
        return Tasklet { _, chunkContext ->

            // Fetch string 'targetDate' from JobParameters and parse it to LocalDate
            val targetDateStr = chunkContext.stepContext.jobParameters["targetDate"] as String
            val targetDate = LocalDate.parse(targetDateStr)

            val salesData = extract(chunkContext, targetDate)
            val totalSales = transform(salesData)
            load(totalSales, targetDate)

            RepeatStatus.FINISHED
        }
    }

    /**
     * Extracts sales data for the target date from the source database.
     *
     * @param chunkContext The ChunkContext containing the job and step context.
     * @return A Pair containing the target date and the list of SalesRecord for the given date.
     * @throws IllegalStateException if no sales data is found for the given target date.
     */
    private fun extract(chunkContext: ChunkContext, targetDate: LocalDate): List<SalesRecord> {
        log.info("Attempting to run job ${chunkContext.stepContext.jobName} with the following date: $targetDate")

        // Fetch List of SalesRecords based on 'targetDate'
        val salesData = salesService.getSalesForDate(targetDate)

        if (salesData.isEmpty()) {
            throw IllegalStateException("No sales data found for the given date: $targetDate")
        }
        return salesData
    }

    /**
     * Transforms the sales data to calculate the total sales amount for the target date.
     *
     * @param salesData The list of SalesRecord to be transformed.
     * @return The total sales amount for the given date.
     */
    private fun transform(salesData: List<SalesRecord>): Double {
        return salesData.sumOf { it.quantity * it.price }
    }

    /**
     * Loads the aggregated data into the target database as a SummaryRecord.
     *
     * @param totalSales The total sales amount to be loaded.
     * @param targetDate The target date for which the summary record is created.
     */
    private fun load(totalSales: Double, targetDate: LocalDate) {
        // Create a new summary record with the aggregated data.
        val summaryRecord = SummaryRecord(totalSales = totalSales, date = targetDate)

        // Write the summary record to the target database.
        summaryService.insertSummary(summaryRecord)
    }

    /**
     * Configures the data source for batch processing.
     *
     * @return The configured data source for batch processing.
     */
    @Qualifier("batchDataSource")
    @Bean
    fun batchDataSource(): DataSource =
        EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("/org/springframework/batch/core/schema-h2.sql")
            .generateUniqueName(true)
            .build()

    /**
     * Configures the transaction manager for batch processing.
     *
     * @param dataSource The data source to be used by the transaction manager.
     * @return The configured transaction manager for batch processing.
     */
    @Bean
    fun batchTransactionManager(@Qualifier("batchDataSource") dataSource: DataSource): JdbcTransactionManager =
        JdbcTransactionManager(dataSource)
}
