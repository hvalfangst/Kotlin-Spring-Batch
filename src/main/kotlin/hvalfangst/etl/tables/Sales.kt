package hvalfangst.etl.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Sales : Table("sales") {
    val id = integer("id").autoIncrement()
    val product = varchar("product", 100)
    val quantity = integer("quantity")
    val price = double("price")
    val date = date("date")
}