package hvalfangst.etl.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

/**
 * Spring configuration class for setting up the data source for the target database.
 */
@Configuration
class TargetDatabaseConfig {

    /**
     * Creates and configures the data source bean for the target database.
     *
     * @param env The EnvironmentConfiguration containing the database connection properties.
     * @return The DataSource bean for the target database.
     */
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
