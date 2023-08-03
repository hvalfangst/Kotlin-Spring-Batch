package hvalfangst.etl.rest

import hvalfangst.etl.model.SummaryRecord
import hvalfangst.etl.service.SummaryService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * REST controller for handling summary data.
 *
 * This controller provides endpoints to retrieve summary data from the SummaryService.
 * It handles requests to get all summaries and summaries for a specific date.
 *
 * @param summaryService The service component responsible for fetching summary data.
 */
@RestController
@RequestMapping("/summary")
class SummaryController(private val summaryService: SummaryService) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * Retrieves all summary records.
     *
     * @return A ResponseEntity containing the list of all summary records or an error response if any.
     */
    @GetMapping
    fun getAllSummaries(): ResponseEntity<List<SummaryRecord>> {
        val allSummaries = summaryService.getAllSummaries()
        return ResponseEntity.ok(allSummaries)
    }

    /**
     * Retrieves summary records for a specific date.
     *
     * @param targetDate The date for which summary records should be retrieved (format: "yyyy-MM-dd").
     * @return A ResponseEntity containing the summary records for the specified date or an error response if any.
     */
    @GetMapping("/{targetDate}")
    fun getSalesForDate(@PathVariable targetDate: String): ResponseEntity<*> {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            val parsedDate = LocalDate.parse(targetDate, dateFormatter)
            val summariesForDate = summaryService.getSummaryForDate(parsedDate)

            when {
                summariesForDate.isEmpty() -> ResponseEntity.notFound().build()
                else -> ResponseEntity.ok(summariesForDate)
            }

        } catch (e: Exception) {
            log.error("Invalid date format: $targetDate")
            ResponseEntity.unprocessableEntity().body("Invalid date format")
        }
    }
}
