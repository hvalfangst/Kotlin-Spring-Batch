package hvalfangst.etl.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.*

data class SummaryRecord(
    @JsonProperty("totalSales") val totalSales: Double,
    @JsonProperty("date") val date: LocalDate
)