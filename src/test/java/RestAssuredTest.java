import app.Account;
import app.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;


public class RestAssuredTest extends TestCase {

    private String userName;
    private Account account;
    private Book book;

    @Before
    public void setEntities() {
        userName = UUID.randomUUID().toString();
        account = new Account(userName, "password");
        book = new Book(account, "Anna Karenina", "Leo Tolstoy", "description");
    }

    @Test
    public void addUser() throws JsonProcessingException {
        this.addUser(account);
    }

    @Test
    public void addBook() throws JsonProcessingException {
        this.addUser(account);
        this.addBookForUser(book, userName);
    }

    @Test
    public void removeBook() throws Exception {
        this.addUser(account);
        this.addBookForUser(book, userName);
        this.deleteUserBook(userName, getLastBookId());
    }

    @Test
    public void removeUser() throws Exception {
        this.addUser(account);
        this.addBookForUser(book, userName);
        this.deleteUserBook(userName, getLastBookId());
        this.removeUser(userName);
    }

    private void removeUser(String userName){
        Response response = given(requestSpec).delete(EndPoints.REMOVE_USER, userName);
        response.prettyPrint();
        response.then().statusCode(SC_OK).log();
    }

    private void addBookForUser(Book book, String userName) throws JsonProcessingException {
        Response response = given(requestSpec).body(mapper.writeValueAsBytes(book)).post(EndPoints.ADD_BOOK, userName);
        response.prettyPrint();
        response.then().statusCode(SC_CREATED).log();
    }

    private void deleteUserBook(String userName, long bookId){
        Response response = given(requestSpec).delete(EndPoints.REMOVE_BOOK, userName, bookId);
        response.prettyPrint();
        response.then().statusCode(SC_OK).log();
    }

    private long getLastBookId() throws Exception {
        Response response = given(requestSpec).get(EndPoints.GET_USERS);
        List<Account> accounts = eitherObjectOrList(response.asString(), Account.class);
        return accounts.get(accounts.size() - 1).getBooks().iterator().next().getId();
    }

    private <A> List<A> eitherObjectOrList(String content, Class<A> c) throws Exception {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(
                content, mapper.getTypeFactory().constructCollectionType(List.class, c)
        );
    }
}