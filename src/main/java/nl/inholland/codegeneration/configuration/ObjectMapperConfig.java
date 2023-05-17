package nl.inholland.codegeneration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.disable;

@Configuration
class ObjectMapperConfig {

    @Primary
    @Bean
    public void configure() {
        ObjectMapper().apply {
            setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
            configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

            val javaTimeModule = JavaTimeModule().apply {
                this.addDeserializer(
                        LocalDateTime:: class.java,
                        LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME)
                )
            }

            registerModules(javaTimeModule, KotlinModule())
        }
    }
}
