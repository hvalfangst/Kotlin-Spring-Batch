package hvalfangst.etl.repository

import hvalfangst.etl.tables.Summary
import hvalfangst.etl.model.SummaryRecord
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.sql.DataSource

/**
 * Repository class for accessing summary data from the target database.
 *
 * @param dataSource The data source used to connect to the target database.
 */
@Repository
class SummaryRepository @Autowired constructor(@Qualifier("targetDatabase") private val dataSource: DataSource) {

    /**
     * This reference is used to explicitly set the current database for the global transaction manager associated with Exposed.
     * Failure to do so will result in it defaulting to the most recent database, which means we are toast.
     */
    private val targetDatabase = Database.connect(dataSource)

    /**
     * Retrieves all summary records from the target database.
     *
     * @return A list of summary records.
     */
    fun getAllSummaries(): List<SummaryRecord> {
        return transaction(targetDatabase) {
            Summary.selectAll().map { it.toSummaryRecord() }
        }
    }

    /**
     * Retrieves summary records from the target database for a specific date.
     *
     * @param targetDate The date for which the summary records should be retrieved.
     * @return A list of summary records for the specified date.
     */
    fun getSummaryForDate(targetDate: LocalDate): List<SummaryRecord> {
        return transaction(targetDatabase) {
            Summary.select { Summary.date eq targetDate }.map {
                it.toSummaryRecord()
            }
        }
    }

    /**
     * Inserts a summary record into the target database.
     *
     * @param summaryRecord The summary record to be inserted.
     */
    fun insertSummary(summaryRecord: SummaryRecord) {
        transaction(targetDatabase) {
            Summary.insert {
                it[totalSales] = summaryRecord.totalSales
                it[date] = summaryRecord.date
            }
        }
    }

    /**
     * Converts a ResultRow to a SummaryRecord object.
     *
     * @param resultRow The ResultRow to be converted.
     * @return A SummaryRecord object representing the data in the ResultRow.
     */
    private fun ResultRow.toSummaryRecord(): SummaryRecord {
        return SummaryRecord(
            totalSales = this[Summary.totalSales],
            date = this[Summary.date]
        )
    }
}
