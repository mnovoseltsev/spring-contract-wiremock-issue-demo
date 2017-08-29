package spring.contract.wiremock.issue.demo

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

import static com.github.tomakehurst.wiremock.client.WireMock.*

@RunWith(SpringRunner)
@SpringBootTest
@AutoConfigureWireMock(port = 0)
class NotOrderedCustomizerTest {

    @Value('${wiremock.server.port}')
    Integer port

    @Autowired
    RestTemplateBuilder restTemplateBuilder

    @Test
    void "Should not fail when ordered customizer added interceptor to rest template"() {
        stubFor(get(urlEqualTo('/some-url'))
                .willReturn(aResponse().withStatus(200).withBody('Yeah!')))

        def client = new RestTemplateClient(restTemplateBuilder.rootUri("http://localhost:$port"))

        def body = client.get()

        assert body == 'Yeah!'
    }
}
