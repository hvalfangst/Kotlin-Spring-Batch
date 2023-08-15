package hvalfangst.etl.config

import hvalfangst.etl.listener.JobNotificationListener
import hvalfangst.etl.tasklet.Extract
import hvalfangst.etl.tasklet.Load
import hvalfangst.etl.tasklet.Transform
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.transaction.PlatformTransactionManager
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
 */
@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
class BatchConfig(private val extract: Extract, private val transform: Transform, private val load: Load) {


    @Bean
    fun extractBean(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step =
        StepBuilder("EXTRACT", jobRepository)
            .tasklet(extract, transactionManager)
            .build()

    @Bean
    fun transformBean(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step =
        StepBuilder("TRANSFORM", jobRepository)
            .tasklet(transform, transactionManager)
            .build()

    @Bean
    fun loadBean(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step =
        StepBuilder("LOAD", jobRepository)
            .tasklet(load, transactionManager)
            .build()



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
            .start(extractBean(jobRepository, transactionManager))
            .next(transformBean(jobRepository, transactionManager))
            .next(loadBean(jobRepository, transactionManager))
            .build()
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
