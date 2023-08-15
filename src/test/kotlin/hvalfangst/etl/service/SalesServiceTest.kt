package hvalfangst.etl.service


import hvalfangst.etl.model.SalesRecord
import hvalfangst.etl.repository.SalesRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
class SalesServiceTest {

    private lateinit var salesRepository: SalesRepository
    private lateinit var salesService: SalesService

    @BeforeEach
    fun setup() {
        salesRepository = mockk(relaxed = true)
        salesService = SalesService(salesRepository)
    }

    @Test
    fun `getAllSales returns list of two SalesRecord on existence of associated data`() {
        val mockSalesRecords = listOf(
            SalesRecord(1, "Product A", 10, 100.0, LocalDate.now()),
            SalesRecord(2, "Product B", 5, 50.0, LocalDate.now())
        )

        every { salesRepository.getAllSales() } returns mockSalesRecords

        val result = salesService.getAllSales()

        assert(result == mockSalesRecords)
    }

    @Test
    fun `getSalesForDate returns list of two SalesRecords on valid date`() {
        val validDate = LocalDate.of(2023, 8, 14)
        val mockSalesRecords = listOf(
            SalesRecord(1, "Product A", 10, 100.0, validDate),
            SalesRecord(2, "Product B", 5, 50.0, validDate)
        )

        every { salesRepository.getSalesForDate(validDate) } returns mockSalesRecords

        val result = salesService.getSalesForDate(validDate)

        assert(result[0].date == validDate)
        assert(result == mockSalesRecords)
    }

    @Test
    fun `getSalesForDate returns an empty list on invalid date`() {
        val validDate = LocalDate.of(2023, 8, 14)
        val invalidDate = LocalDate.of(2025, 9, 10)
        val mockSalesRecords = listOf(
            SalesRecord(1, "Product A", 10, 100.0, validDate),
            SalesRecord(2, "Product B", 5, 50.0, validDate)
        )

        every { salesRepository.getSalesForDate(validDate) } returns mockSalesRecords

        val result: List<SalesRecord> = salesService.getSalesForDate(invalidDate)
        assert(result.isEmpty())
    }

    @Test
    fun `getSalesForDate throws exception on null date`() {
        val exception = assertThrows<NullPointerException> {
            salesService.getSalesForDate(null)
        }

        assert(exception.message == "Variable 'targetDate' is null")
    }
}
