//package hvalfangst.batch.cron
//
//import org.springframework.batch.core.Job
//import org.springframework.batch.core.JobParametersBuilder
//import org.springframework.batch.core.launch.JobLauncher
//import org.springframework.scheduling.annotation.Scheduled
//import org.springframework.stereotype.Component
//
//@Component
//class ScheduledJobTrigger(private val jobLauncher: JobLauncher, private val calculateRevenueJob: Job) {
//
//    @Scheduled(cron = "0 * * * * ?") // Run the job every minute
//    fun runJob() {
//        val jobParameters = JobParametersBuilder()
//            .addLong("timestamp", System.currentTimeMillis())
//            .toJobParameters()
//
//        jobLauncher.run(calculateRevenueJob, jobParameters)
//    }
//}
