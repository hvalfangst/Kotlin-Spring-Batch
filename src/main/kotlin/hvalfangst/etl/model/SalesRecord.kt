package hvalfangst.etl.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDate

data class SalesRecord(
    @JsonProperty("id") val id: Int,
    @JsonProperty("product") val product: String,
    @JsonProperty("quantity") val quantity: Int,
    @JsonProperty("price") val price: Double,
    @JsonProperty("date") val date: LocalDate
) : Serializable