package spring.contract.wiremock.issue.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@SpringBootApplication
public class SpringContractWiremockIssueDemoApplication {

    private static final int SOME_NOT_LOWEST_PRECEDENCE = Ordered.LOWEST_PRECEDENCE - 1;

    public static void main(String[] args) {
        SpringApplication.run(SpringContractWiremockIssueDemoApplication.class, args);
    }

    @Bean
    @Order(SOME_NOT_LOWEST_PRECEDENCE)
    @Profile("bug")
    public RestTemplateCustomizer someOrderedInterceptorCustomizer() {
        return restTemplate -> {
            ClientHttpRequestInterceptor emptyInterceptor = (request, body, execution) -> execution.execute(request, body);

            restTemplate.getInterceptors().add(emptyInterceptor);
        };
    }

    @Bean
    public RestTemplateCustomizer someNotOrderedInterceptorCustomizer() {
        return restTemplate -> {
            ClientHttpRequestInterceptor emptyInterceptor = (request, body, execution) -> execution.execute(request, body);

            restTemplate.getInterceptors().add(emptyInterceptor);
        };
    }

    @Bean
    @Order
    public RestTemplateCustomizer someLowestPrecedenceOrderedInterceptorCustomizer() {
        return restTemplate -> {
            ClientHttpRequestInterceptor emptyInterceptor = (request, body, execution) -> execution.execute(request, body);

            restTemplate.getInterceptors().add(emptyInterceptor);
        };
    }
}
