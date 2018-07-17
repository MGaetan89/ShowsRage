import com.android.build.gradle.internal.dsl.SigningConfig
import org.gradle.api.Action
import java.io.File
import java.util.Properties

object SigningConfigs {
    object Release : Action<SigningConfig> {
        override fun execute(signingConfig: SigningConfig) {
            val localPropertiesFile = File("local.properties")
            val properties = Properties()
            if (localPropertiesFile.exists()) {
                properties.load(localPropertiesFile.inputStream())
            }

            signingConfig.keyAlias = properties.getProperty("signing.keyAlias", "")
            signingConfig.keyPassword = properties.getProperty("signing.keyPassword", "")
            signingConfig.storeFile = File(properties.getProperty("signing.storeFile", "-"))
            signingConfig.storePassword = properties.getProperty("signing.storePassword", "")
        }
    }
}
