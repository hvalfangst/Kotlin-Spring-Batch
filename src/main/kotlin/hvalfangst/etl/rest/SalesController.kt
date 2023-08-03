package hvalfangst.etl.rest

import hvalfangst.etl.model.SalesRecord
import hvalfangst.etl.service.SalesService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * REST controller for handling sales data.
 *
 * This controller provides endpoints to retrieve sales data from the SalesService.
 * It handles requests to get all sales and sales data for a specific date.
 *
 * @param salesService The service component responsible for fetching sales data.
 */
@RestController
@RequestMapping("/sales")
class SalesController(private val salesService: SalesService) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * Retrieves all sales records.
     *
     * @return A ResponseEntity containing the list of all sales records or an error response if any.
     */
    @GetMapping
    fun getAllSales(): ResponseEntity<List<SalesRecord>> {
        val allSales = salesService.getAllSales()
        return ResponseEntity.ok(allSales)
    }

    /**
     * Retrieves sales records for a specific date.
     *
     * @param targetDate The date for which sales records should be retrieved (format: "yyyy-MM-dd").
     * @return A ResponseEntity containing the sales records for the specified date or an error response if any.
     */
    @GetMapping("/{targetDate}")
    fun getSalesForDate(@PathVariable targetDate: String): ResponseEntity<*> {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return try {
            val parsedDate = LocalDate.parse(targetDate, dateFormatter)
            val salesForDate = salesService.getSalesForDate(parsedDate)

            when {
                salesForDate.isEmpty() -> ResponseEntity.notFound().build()
                else -> ResponseEntity.ok(salesForDate)
            }

        } catch (e: Exception) {
            log.error("Invalid date format: $targetDate")
            ResponseEntity.unprocessableEntity().body("Invalid date format")
        }
    }
}
