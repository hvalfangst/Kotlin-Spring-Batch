package hvalfangst.etl.service

import hvalfangst.etl.model.SummaryRecord
import hvalfangst.etl.repository.SummaryRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * Service class for handling business logic related to SummaryRecord objects.
 *
 * @param summaryRepository The SummaryRepository used to access the database and retrieve summary data.
 */
@Service
class SummaryService(private val summaryRepository: SummaryRepository) {

    /**
     * Retrieves all summary records from the database.
     *
     * @return A list of all summary records.
     */
    fun getAllSummaries(): List<SummaryRecord> {
        return summaryRepository.getAllSummaries()
    }

    /**
     * Retrieves summary records from the database for a specific date.
     *
     * @param targetDate The date for which the summary records should be retrieved.
     * @return A list of summary records for the specified date.
     */
    fun getSummaryForDate(targetDate: LocalDate): List<SummaryRecord> {
        return summaryRepository.getSummaryForDate(targetDate)
    }

    /**
     * Inserts a single summary record into the target database.
     *
     * @param summaryRecord The summary record to be inserted.
     */
    fun insertSummary(summaryRecord: SummaryRecord) {
        summaryRepository.insertSummary(summaryRecord)
    }

}
