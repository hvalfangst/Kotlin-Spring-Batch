package hvalfangst.etl.tasklet

import hvalfangst.etl.model.SalesRecord
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

@Component
class Transform : Tasklet, StepExecutionListener {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(Extract::class.java)
        private var salesData: List<SalesRecord> = emptyList()
        private var totalSales: Double? = null
    }

    @BeforeStep
    override fun beforeStep(stepExecution: StepExecution) {
        log.info("[TRANSFORM] BeforeStep: Fetching sales data from context")
        val executionContext = stepExecution.jobExecution.executionContext
        salesData = executionContext["salesData"] as List<SalesRecord>
    }

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        log.info("[TRANSFORM] executing Tasklet")
        totalSales = salesData.sumOf { it.quantity * it.price }
        return RepeatStatus.FINISHED
    }

    @AfterStep
    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        log.info("[TRANSFORM] AfterStep: Putting totalSales on context")
        val executionContext = stepExecution.jobExecution.executionContext
        executionContext.put("totalSales", totalSales)
        return ExitStatus.COMPLETED
    }
}