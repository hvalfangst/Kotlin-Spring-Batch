package hvalfangst.batch.db

import hvalfangst.batch.model.SummaryRecord
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class SummaryRowMapper : RowMapper<SummaryRecord> {
    override fun mapRow(rs: ResultSet, rowNum: Int): SummaryRecord {
        return SummaryRecord(
            id = rs.getInt("id"),
            totalSales = rs.getDouble("totalSales"),
            date = rs.getDate("date").toLocalDate()
        )
    }
}

