import java.nio.charset.StandardCharsets
import java.util.Base64

import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the Location entity.
 */
class LocationGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://127.0.0.1:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connection("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

        val authorization_header = "Basic " + Base64.getEncoder.encodeToString("adapapp:mySecretOAuthSecret".getBytes(StandardCharsets.UTF_8))

    val headers_http_authentication = Map(
        "Content-Type" -> """application/x-www-form-urlencoded""",
        "Accept" -> """application/json""",
        "Authorization"-> authorization_header
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "Bearer ${access_token}"
    )

    val scn = scenario("Test the Location entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401)))
        .pause(10)
        .exec(http("Authentication")
        .post("/oauth/token")
        .headers(headers_http_authentication)
        .formParam("username", "admin")
        .formParam("password", "admin")
        .formParam("grant_type", "password")
        .formParam("scope", "read write")
        .formParam("client_secret", "mySecretOAuthSecret")
        .formParam("client_id", "adapapp")
        .formParam("submit", "Login")
        .check(jsonPath("$.access_token").saveAs("access_token")))
        .pause(1)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all locations")
            .get("/api/locations")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new location")
            .post("/api/locations")
            .headers(headers_http_authenticated)
            .body(StringBody("""{"id":null, "isprimary":null, "address1":"SAMPLE_TEXT", "address2":"SAMPLE_TEXT", "cityname":"SAMPLE_TEXT", "citynamealias":"SAMPLE_TEXT", "countyname":"SAMPLE_TEXT", "countyfips":"SAMPLE_TEXT", "countyansi":"SAMPLE_TEXT", "statename":"SAMPLE_TEXT", "statecode":"SAMPLE_TEXT", "statefips":"SAMPLE_TEXT", "stateiso":"SAMPLE_TEXT", "stateansi":"SAMPLE_TEXT", "zippostcode":"SAMPLE_TEXT", "countryname":"SAMPLE_TEXT", "countryiso2":"SAMPLE_TEXT", "countryiso3":"SAMPLE_TEXT", "latitudedd":null, "longitudedd":null, "status":null, "lastmodifiedby":"SAMPLE_TEXT", "lastmodifieddate":"2020-01-01T00:00:00.000Z", "domain":"SAMPLE_TEXT"}""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_location_url")))
            .pause(10)
            .repeat(5) {
                exec(http("Get created location")
                .get("${new_location_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created location")
            .delete("${new_location_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(100) over (1 minutes))
    ).protocols(httpConf)
}
