package hvalfangst.etl.repository

import hvalfangst.etl.tables.Sales
import hvalfangst.etl.model.SalesRecord
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.sql.DataSource

/**
 * Repository class for accessing sales data from the source database.
 *
 * @param dataSource The data source used to connect to the source database.
 */
@Repository
class SalesRepository @Autowired constructor(@Qualifier("sourceDatabase") private val dataSource: DataSource) {

    /**
     * This reference is used to explicitly set the current database for the global transaction manager associated with Exposed.
     * Failure to do so will result in it defaulting to the most recent database, which means we are toast.
     */
    private val sourceDatabase = Database.connect(dataSource)

    /**
     * Retrieves all sales records from the source database.
     *
     * @return A list of sales records.
     */
    fun getAllSales(): List<SalesRecord> {
        return transaction(sourceDatabase) {
            Sales.selectAll().map { it.toSalesRecord() }
        }
    }

    /**
     * Retrieves sales records from the source database for a specific date.
     *
     * @param targetDate The date for which the sales records should be retrieved.
     * @return A list of sales records for the specified date.
     */
    fun getSalesForDate(targetDate: LocalDate): List<SalesRecord> {
        return transaction(sourceDatabase) {
            Sales.select { Sales.date eq targetDate }.map {
                it.toSalesRecord()
            }
        }
    }

    /**
     * Converts a ResultRow to a SalesRecord object.
     *
     * @return A SalesRecord object representing the data in the ResultRow.
     */
    private fun ResultRow.toSalesRecord(): SalesRecord {
        return SalesRecord(
            id = this[Sales.id],
            product = this[Sales.product],
            quantity = this[Sales.quantity],
            price = this[Sales.price],
            date = this[Sales.date]
        )
    }
}
