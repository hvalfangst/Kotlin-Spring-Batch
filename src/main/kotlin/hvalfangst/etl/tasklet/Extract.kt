package hvalfangst.etl.tasklet

import hvalfangst.etl.model.SalesRecord
import hvalfangst.etl.service.SalesService
import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.annotation.AfterStep
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class Extract(private val salesService: SalesService): Tasklet, StepExecutionListener {

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private var targetDate: LocalDate? = null
        private var salesData: List<SalesRecord> = emptyList()
    }

    @BeforeStep
    override fun beforeStep(stepExecution: StepExecution) {
        log.info("[EXTRACT] BeforeStep: Fetching date from job parameters")
        val targetDateStr = stepExecution.jobParameters.getString("targetDate") as String
        targetDate = LocalDate.parse(targetDateStr)
    }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        log.info("[EXTRACT] executing Tasklet")
            salesData = salesService.getSalesForDate(targetDate)
            return RepeatStatus.FINISHED
    }

    @AfterStep
    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        log.info("[EXTRACT] AfterStep: Putting list of SalesRecords in executionContext")
        val executionContext = stepExecution.jobExecution.executionContext
        executionContext.put("salesData", salesData)
        return ExitStatus.COMPLETED
    }
}