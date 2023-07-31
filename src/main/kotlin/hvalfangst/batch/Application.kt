package hvalfangst.batch

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class Application() : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {}
}

fun main(args: Array<String>) {
    // Return the batch ExitCode as the application's process exit code
    exitProcess(SpringApplication.exit(runApplication<Application>(*args)))
}
