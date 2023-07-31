package hvalfangst.batch.listener

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component

@Component
class JobNotificationListener(): JobExecutionListener {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("Starting batch job with id [${jobExecution.id}]")
        super.beforeJob(jobExecution)
    }

    override fun afterJob(jobExecution: JobExecution) {
        log.info("Batch job with id [${jobExecution.id}] finished with status [${jobExecution.status}]")
        super.afterJob(jobExecution)
    }
}