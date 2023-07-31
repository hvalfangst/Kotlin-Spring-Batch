package hvalfangst.batch.config

import org.jetbrains.exposed.sql.Database
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class TargetDatabaseConfig(env: EnvironmentConfiguration) {
    init {
        Database.connect(
            url = env.targetDatabase.url,
            driver = env.targetDatabase.driver,
            user = env.targetDatabase.username,
            password = env.targetDatabase.password
        )
    }

    @Qualifier("targetDatabase")
    @Bean(name = ["targetDatabase"])
    fun targetDatabase(env: EnvironmentConfiguration): DataSource {
        return DriverManagerDataSource().apply {
            setDriverClassName(env.targetDatabase.driver)
            url = env.targetDatabase.url
            username = env.targetDatabase.username
            password = env.targetDatabase.password
        }
    }
}
