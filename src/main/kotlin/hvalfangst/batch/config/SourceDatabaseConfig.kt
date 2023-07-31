package hvalfangst.batch.config

import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class SourceDatabaseConfig(env: EnvironmentConfiguration) {
    init {
        Database.connect(
            url = env.sourceDatabase.url,
            driver = env.sourceDatabase.driver,
            user = env.sourceDatabase.username,
            password = env.sourceDatabase.password
        )
    }

    @Qualifier("sourceDatabase")
    @Bean(name = ["sourceDatabase"])
    fun sourceDatabase(env: EnvironmentConfiguration): DataSource {
        return DriverManagerDataSource().apply {
            setDriverClassName(env.sourceDatabase.driver)
            url = env.sourceDatabase.url
            username = env.sourceDatabase.username
            password = env.sourceDatabase.password
        }
    }
}


