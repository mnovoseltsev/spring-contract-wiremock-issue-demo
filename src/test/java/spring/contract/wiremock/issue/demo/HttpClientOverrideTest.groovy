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
import static spring.contract.wiremock.issue.demo.RestTemplateClient.SOME_PATH

@RunWith(SpringRunner)
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@ActiveProfiles(['http-client-override', 'issue']) //remove 'issue' profile to have green test
class HttpClientOverrideTest {

    @Value('${wiremock.server.port}')
    Integer port

    @Autowired
    RestTemplateBuilder restTemplateBuilder

    @Test
    void "Should not process Set-Cookie header from response, because we configured HttpClient to do so"() {

        def someCookie = 'ABC'

        stubFor(get(urlPathEqualTo(SOME_PATH)).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader('Set-Cookie', "$someCookie=123")))

        def client = new RestTemplateClient(restTemplateBuilder.rootUri("http://localhost:$port"))

        3.times { client.get() }

        verify(exactly(3), getRequestedFor(urlPathMatching(SOME_PATH))
                .withCookie(someCookie, absent()))
    }
}
