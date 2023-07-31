package hvalfangst.batch.config


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "env")
class EnvironmentConfiguration {
        lateinit var sourceDatabase: SourceDatabaseProperties
        lateinit var targetDatabase: TargetDatabaseProperties
}

class SourceDatabaseProperties {
        var url: String = "nil"
        var username: String = "nil"
        var password: String = "nil"
        var driver: String = "nil"
}


class TargetDatabaseProperties {
        var url: String = "nil"
        var username: String = "nil"
        var password: String = "nil"
        var driver: String = "nil"
}
