package hvalfangst.batch.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Summary : Table("summary") {
    val id = Sales.integer("id").autoIncrement()
    val totalSales = double("totalSales")
    val date = date("date")
}
