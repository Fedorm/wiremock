import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.notMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by fedorm on 20.08.2017.
 */
public class BasicTest {

  //jUnit rule. Service create before starting each test and down when test finished
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(8089);

  @Test
  public void simpleTest() {
    stubFor(any(urlEqualTo("/api/auth"))
        .willReturn(okJson("{ \"apiKey\": \"749d94d02e0e3104b8373c8eb1bed085\" }")));

    //Rest-assured create post request for testing mock service and send request on mock service
    Response response = RestAssured.given().
        header(new Header("Content-Type", "application/json")).
        body("{name: 'test'}")
        .post("http://localhost:8089/api/auth");
    response.statusLine();
    System.out.println(response.headers().toString());
    response.body().prettyPrint();

    //Verify request
    verify(postRequestedFor(urlMatching("/api/auth"))
        .withRequestBody(matching("\\{name: .*\\}"))
        .withHeader("Content-Type", notMatching("application/json")));
  }
}
