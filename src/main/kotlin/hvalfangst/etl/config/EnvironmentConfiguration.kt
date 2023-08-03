package hvalfangst.etl.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Configuration class for holding properties related to the source and target databases.
 * The prefix "env" is used to locate the properties in the application properties file.
 */
@Component
@ConfigurationProperties(prefix = "env")
class EnvironmentConfiguration {
        lateinit var sourceDatabase: SourceDatabaseProperties
        lateinit var targetDatabase: TargetDatabaseProperties
}

/**
 * Nested class representing the properties for the source database.
 */
class SourceDatabaseProperties {
        var url: String = "nil"
        var username: String = "nil"
        var password: String = "nil"
        var driver: String = "nil"
        var migrationPath: String = "nil"
}

/**
 * Nested class representing the properties for the target database.
 */
class TargetDatabaseProperties {
        var url: String = "nil"
        var username: String = "nil"
        var password: String = "nil"
        var driver: String = "nil"
        var migrationPath: String = "nil"
}
