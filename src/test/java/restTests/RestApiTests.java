package restTests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.registerParser;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class RestApiTests {

    @Test
    void getListUsers() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
    }

    @Test
    void createUsers() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"));
    }

    @Test
    void updateUserInfo() {
        String data = "{\"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("job", is("zion resident"));
    }

    @Test
    void deleteUsers() {
        given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void unsuccessfulRegister() {
        String data = "{\"email\": \"test@mail.ru\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

}
