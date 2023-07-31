package hvalfangst.batch.config

import jakarta.annotation.PostConstruct
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class FlywayConfig {

        private val log = LoggerFactory.getLogger(this.javaClass)

        @Autowired
        private val sourceDatabase: DataSource? = null

        @Autowired
        private val targetDatabase: DataSource? = null

        @PostConstruct
        fun migrateFlyway() {
            log.info("Migrate [SOURCE DATABASE]")
            var configuration = ClassicConfiguration()
            configuration.dataSource = sourceDatabase
            configuration.setLocations(Location("filesystem:./migrations/source"))
            var flyway = Flyway(configuration)
            flyway.migrate()

            log.info("Migrate [TARGET DATABASE]")
            configuration = ClassicConfiguration()
            configuration.dataSource = targetDatabase
            configuration.setLocations(Location("filesystem:./migrations/target"))
            flyway = Flyway(configuration)
            flyway.migrate()
        }
    }
