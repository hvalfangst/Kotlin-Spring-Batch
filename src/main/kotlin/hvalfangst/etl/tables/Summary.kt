package hvalfangst.etl.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Summary : Table("summary") {
    val id = integer("id").autoIncrement()
    val totalSales = double("total_sales")
    val date = date("date")
}
