import app.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;

public abstract class TestCase {

    protected static RequestSpecification requestSpec;

    protected ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void createSpecification() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8080)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    protected void addUser(Account user) throws JsonProcessingException {
        Response response = given(requestSpec).body(mapper.writeValueAsBytes(user)).post(EndPoints.ADD_USER);
        response.then().statusCode(SC_CREATED).log();
    }

}