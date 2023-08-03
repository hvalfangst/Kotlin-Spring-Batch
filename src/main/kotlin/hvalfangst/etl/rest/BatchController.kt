package hvalfangst.etl.rest

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * REST controller for triggering batch jobs.
 *
 * This controller provides an endpoint to start a specific batch job using the Spring Batch framework.
 * It allows the job to be triggered by sending a POST request to the '/batch/start' endpoint.
 *
 * @param jobLauncher The JobLauncher responsible for launching batch jobs.
 * @param job The specific Job bean that needs to be executed when the endpoint is triggered.
 */
@RestController
@RequestMapping("/batch")
class BatchController(@Autowired private val jobLauncher: JobLauncher,
                      @Autowired private val job: Job
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * Starts the specified batch job when the endpoint is called.
     *
     * @return A response entity indicating the status of the job execution.
     */
    @PostMapping("/start")
    fun startBatchJob(@RequestParam("targetDate") targetDate: String): ResponseEntity<String> {
        return try {

            // Parse the input date string into LocalDate
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val parsedDate = LocalDate.parse(targetDate, dateFormatter)

            // Generate JobParameters with the parsed date.
            val jobParameters = JobParametersBuilder()
                .addString("targetDate", parsedDate.toString())
                .toJobParameters()

            // Run the job with the generated JobParameters.
            val jobExecution: JobExecution = jobLauncher.run(job, jobParameters)

            // Check the status of the job and return the appropriate response.
            if (jobExecution.status.isUnsuccessful) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job failed with status: ${jobExecution.status}")
            } else {
                ResponseEntity.ok("Batch job started successfully!")
            }

        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while starting the job: ${e.message}")
        }
    }
}
