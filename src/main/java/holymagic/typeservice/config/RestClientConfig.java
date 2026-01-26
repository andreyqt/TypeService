package holymagic.typeservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${ape_Key}")
    private String apeKey;
    @Value("${base_url}")
    private String baseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(header -> {
                    header.add("Authorization", apeKey);
                    header.add("Content-Type", "application/json");
                })
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, handleClientError())
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, handleServerError())
                .defaultStatusHandler(HttpStatusCode::is2xxSuccessful, handleSuccess())
                .build();
    }

    public RestClient.ResponseSpec.ErrorHandler handleClientError() {
        return (request, response) -> {
            HttpStatusCode statusCode = response.getStatusCode();
            String statusText = response.getStatusText();
            log.error("Bad request! \n method: {} \n uri: {} \n code: {}, {} \n timestamp: {}",
                    request.getMethod(), request.getURI(), statusCode, statusText, LocalDateTime.now());
            throw new HttpClientErrorException(statusCode, statusText);
        };
    }

    public RestClient.ResponseSpec.ErrorHandler handleServerError() {
        return (_, response) -> {
            HttpStatusCode statusCode = response.getStatusCode();
            String statusText = response.getStatusText();
            log.error("Server failed to complete request! \n code: {}, text: {} \n timestamp: {}",
                    statusCode, statusText, LocalDateTime.now()
            );
            throw new HttpServerErrorException(statusCode, statusText);
        };
    }

    public RestClient.ResponseSpec.ErrorHandler handleSuccess() {
        return (request, _) -> log.info("Made successful request! \n method: {} \n uri: {}",
                request.getMethod(), request.getURI());
    }

}
