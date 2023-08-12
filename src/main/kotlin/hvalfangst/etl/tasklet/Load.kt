package hvalfangst.etl.tasklet

import hvalfangst.etl.model.SummaryRecord
import hvalfangst.etl.service.SummaryService
import org.slf4j.Logger
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
class Load(val summaryService: SummaryService) : Tasklet, StepExecutionListener {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(Extract::class.java)
        private var date: LocalDate = LocalDate.MAX
        private var totalSales: Double = 0.0
    }

    @BeforeStep
    override fun beforeStep(stepExecution: StepExecution) {
        log.info("[LOAD] BeforeStep: Fetching date and totalSales from context")
        val targetDateStr = stepExecution.jobParameters.getString("targetDate") as String
        date = LocalDate.parse(targetDateStr)

        val executionContext = stepExecution.jobExecution.executionContext
        totalSales = executionContext["totalSales"] as Double
    }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        log.info("[LOAD] executing Tasklet")
        summaryService.insertSummary(SummaryRecord(totalSales, date))
        return RepeatStatus.FINISHED
    }

    @AfterStep
    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        return ExitStatus.COMPLETED
    }
}