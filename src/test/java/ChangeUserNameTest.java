import app.Account;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class ChangeUserNameTest extends TestCase {

    private String userName;
    private  Account account;

    @Before
    public void before() {
        userName = UUID.randomUUID().toString();
        account = new Account(userName, "password");
    }

    @Test
    public void testChangeName() throws IOException, InterruptedException {
        this.addUser(account);
        account.setUsername("NEW_NAME");
        this.changeUserName(userName, account);
    }

    private void changeUserName(String userName, Account account) throws IOException {
        Response response = given(requestSpec).body(mapper.writeValueAsBytes(account)).put(EndPoints.CHANGE_USER, userName);
        response.then().statusCode(SC_OK).log();
    }

}