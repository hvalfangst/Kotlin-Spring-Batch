package hvalfangst.etl.service

import hvalfangst.etl.model.SalesRecord
import hvalfangst.etl.repository.SalesRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * Service class for handling business logic related to SalesRecord objects.
 *
 * @param salesRepository The SalesRepository used to access the database and retrieve sales data.
 */
@Service
class SalesService(private val salesRepository: SalesRepository) {

    /**
     * Retrieves all sales records from the database.
     *
     * @return A list of all sales records.
     */
    fun getAllSales(): List<SalesRecord> {
        return salesRepository.getAllSales()
    }

    /**
     * Retrieves sales records from the database for a specific date.
     *
     * @param targetDate The date for which the sales records should be retrieved.
     * @return A list of sales records for the specified date.
     */
    fun getSalesForDate(targetDate: LocalDate?): List<SalesRecord> {
        when(targetDate) {
            null -> throw java.lang.NullPointerException("Variable 'targetDate' is null")
            else ->  return salesRepository.getSalesForDate(targetDate)
        }
    }
}
