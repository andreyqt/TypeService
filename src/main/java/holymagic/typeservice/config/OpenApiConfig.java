package holymagic.typeservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Application to interact with MonkeyType API")
                        .version("1.0")
                        .description("""
                        General documentation:
                        API gives access to:
                        1) user account data;
                        2) user test results;
                        3) Public data.
                        """
                        )
                        .contact(new Contact()
                                .name("HolyMagic1997")
                                .url("https://vk.com/holycool"))

                )
                .servers(List.of(new Server()
                        .url(devUrl)
                        .description("Local Server"))
                );
    }
}
