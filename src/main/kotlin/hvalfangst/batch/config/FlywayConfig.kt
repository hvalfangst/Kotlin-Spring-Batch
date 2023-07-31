package hvalfangst.batch.config

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig {

    @Bean(initMethod = "migrate")
    fun source(@Qualifier("sourceDatabase") dataSource: DataSource): Flyway {
        return Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/source")
            .load()
    }

    @Bean(initMethod = "migrate")
    fun target(@Qualifier("targetDatabase") dataSource: DataSource): Flyway {
        return Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration/target")
            .load()
    }
}