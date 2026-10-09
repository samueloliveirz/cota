package com.samueloliverz.Cota.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
public class CnpjClient {

    private final RestClient restClient;

    public CnpjClient() {
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofSeconds(3));
        fabrica.setReadTimeout(Duration.ofSeconds(5));

        this.restClient = RestClient.builder()
                .baseUrl("https://publica.cnpj.ws/cnpj")
                .requestFactory(fabrica)
                .defaultHeader(HttpHeaders.USER_AGENT, "Cota/1.0 (sistema de orcamentos)")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Optional<CnpjWsResposta> buscar(String cnpj) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/{cnpj}", cnpj)
                    .retrieve()
                    .body(CnpjWsResposta.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            log.warn("Falha ao consultar CNPJ {} no CNPJ.ws: {}", cnpj, e.getMessage());
            return Optional.empty();
        }
    }
}