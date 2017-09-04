package spring.contract.wiremock.issue.demo;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestTemplateClient {

    public static final String SOME_PATH = "/some-url";

    private final RestTemplate restTemplate;

    public RestTemplateClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String get() {

        return restTemplate.getForObject(SOME_PATH, String.class);
    }
}
