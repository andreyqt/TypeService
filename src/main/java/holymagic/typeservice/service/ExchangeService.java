package holymagic.typeservice.service;

import holymagic.typeservice.exception.DataValidationException;
import holymagic.typeservice.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final RestClient restClient;

    public <T> T makeGetRequest(URI uri, ParameterizedTypeReference<Response<T>> responseType) {
        Response<T> response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(responseType);
        validateResponse(response);
        return response.getData();
    }

    private <T> void validateResponse(Response<T> response) {
        if (response.getData() == null) {
            throw new DataValidationException("Response data is null");
        }
    }

}
