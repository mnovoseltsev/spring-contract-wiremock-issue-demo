package spring.contract.wiremock.issue.demo;

import lombok.val;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@SpringBootApplication
public class SpringContractWiremockIssueDemoApplication {

    private static final int SOME_NOT_LOWEST_PRECEDENCE = Ordered.LOWEST_PRECEDENCE - 1;

    public static void main(String[] args) {
        SpringApplication.run(SpringContractWiremockIssueDemoApplication.class, args);
    }

    @Configuration
    @Profile("interceptor")
    public static class InterceptorIssue {
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

    @Configuration
    @Profile("http-client-override")
    public static class OverrideIssue {

        @Bean
        @Order(SOME_NOT_LOWEST_PRECEDENCE)
        @Profile("issue")
        public RestTemplateCustomizer someOrderedInterceptorCustomizer() {
            return restTemplate -> {
                val httpClient = HttpClientBuilder.create()
                        .disableCookieManagement()
                        .build();

                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
            };
        }

        @Bean
        @Profile("!issue")
        public RestTemplateCustomizer someNotOrderedInterceptorCustomizer() {
            return restTemplate -> {
                val httpClient = HttpClientBuilder.create()
                        .disableCookieManagement()
                        .build();

                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
            };
        }
    }
}
