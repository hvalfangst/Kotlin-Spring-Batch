package hvalfangst.etl.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

/**
 * Job notification listener for Spring Batch jobs.
 *
 * This class implements the JobExecutionListener interface to receive notifications about job execution events.
 * It logs information before and after the job execution.
 */
@Component
class JobNotificationListener : JobExecutionListener {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Invoked before the job is executed.
     *
     * @param jobExecution The JobExecution object representing the current job execution.
     */
    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Starting ETL job with id [${jobExecution.id}]")
    }

    /**
     * Invoked after the job is executed.
     *
     * @param jobExecution The JobExecution object representing the current job execution.
     */
    override fun afterJob(jobExecution: JobExecution) {
        log.info("Batch job with id [${jobExecution.id}] finished with status [${jobExecution.status}]")
    }
}
