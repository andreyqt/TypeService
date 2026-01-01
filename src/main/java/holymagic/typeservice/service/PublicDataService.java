package holymagic.typeservice.service;

import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.validator.PublicDataValidator;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Map;

import static holymagic.typeservice.model.ParameterizedTypeReferences.SPEED_HISTOGRAM_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.TYPING_STATS_REF;

@Service
@RequiredArgsConstructor
public class PublicDataService {
    private final RestClient restClient;
    private final PublicDataValidator publicDataValidator;

    public Map<String, Integer> getSpeedHistogram(String language, String mode, String mode2) {
        publicDataValidator.validateGetHistogramArgs(language, mode, mode2);
        URI uri = UriBuilder.fromPath("/public/speedHistogram")
                .queryParam("language", language)
                .queryParam("mode", mode)
                .queryParam("mode2", mode2)
                .build();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(SPEED_HISTOGRAM_REF)
                .getData();
    }

    public TypingStats getTypingStats() {
        URI uri = UriBuilder.fromPath("/public/typingStats")
                .build();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(TYPING_STATS_REF)
                .getData();
    }
}
