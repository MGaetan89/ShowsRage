import java.io.File
import java.util.Properties

object SigningConfigs {
    object Release {
        val keyAlias: String
        val keyPassword: String
        val storeFile: File
        val storePassword: String

        init {
            val properties = Properties()
            properties.load(File("local.properties").inputStream())

            this.keyAlias = properties.getProperty("signing.keyAlias", "")
            this.keyPassword = properties.getProperty("signing.keyPassword", "")
            this.storeFile = File(properties.getProperty("signing.storeFile", "-"))
            this.storePassword = properties.getProperty("signing.storePassword", "")
        }
    }
}
