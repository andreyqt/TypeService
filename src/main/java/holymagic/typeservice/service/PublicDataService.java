package holymagic.typeservice.service;

import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.validator.HttpParamValidator;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;

import static holymagic.typeservice.model.ParameterizedTypeReferences.SPEED_HISTOGRAM_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.TYPING_STATS_REF;

@Service
@RequiredArgsConstructor
public class PublicDataService {

    private final ExchangeService exchangeService;
    private final HttpParamValidator httpParamValidator;

    public Map<String, Integer> getSpeedHistogram(String language, String mode, String mode2) {
        httpParamValidator.validatePublicDataArgs(language, mode, mode2);
        URI uri = UriBuilder.fromPath("/public/speedHistogram")
                .queryParam("language", language)
                .queryParam("mode", mode)
                .queryParam("mode2", mode2)
                .build();
        return exchangeService.makeGetRequest(uri, SPEED_HISTOGRAM_REF);
    }

    public TypingStats getTypingStats() {
        URI uri = UriBuilder.fromPath("/public/typingStats")
                .build();
        return exchangeService.makeGetRequest(uri, TYPING_STATS_REF);
    }


}
