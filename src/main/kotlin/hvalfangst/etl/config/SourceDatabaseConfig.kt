package hvalfangst.etl.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

/**
 * Spring configuration class for setting up the data source for the source database.
 */
@Configuration
class SourceDatabaseConfig {

    /**
     * Creates and configures the data source bean for the source database.
     *
     * @param env The EnvironmentConfiguration containing the database connection properties.
     * @return The DataSource bean for the source database.
     */
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
