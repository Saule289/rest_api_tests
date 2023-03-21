package rest;

import models.ResponseModel;
import models.SuccessfulLogin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static spec.Specification.*;
import static org.hamcrest.Matchers.hasItem;


public class Groovy {
    @Tag("remote")
    @DisplayName("Single user not found")
    @Test
    void getNoExistingUser() {
        step("Отправить get-запрос на поиск отсутствующего юзера", () -> {
            given()
                    .spec(requestSpec)
                    .get("/users/23")
                    .then()
                    .spec(responseSpec404)
                    .log().body();

        });
    }

    @Tag("remote")
    @DisplayName("Single user not found")
    @Test
    public void checkNameOfUser() {
        step("Поиск юзера по имени", () -> {
            given()
                    .spec(requestSpec)
                    .get("/users?=page=2")
                    .then()
                    .spec(responseSpec200)
                    .log().body()
                    .body("data.findAll{it.first_name}.first_name.flatten()", hasItem("Lindsay"));

        });
    }

    @Test
    public void checkAvatarOfUsers() {
        step("Поиск юзера по аватару", () -> {
            given()
                    .spec(requestSpec)
                    .when()
                    .get("/users?=page=2")
                    .then()
                    .log().body()
                    .body("data.findAll{it.avatar.startsWith('https://reqres.in')}.avatar.flatten()",
                            hasItem("https://reqres.in/img/faces/11-image.jpg"));
        });
    }

    @Test
    public void checkLastNameOfUser() {
        step("Поиск юзера по фамилии", () -> {
            given()
                    .spec(requestSpec)
                    .when()
                    .get("/users?=page=2")
                    .then()
                    .log().body()
                    .body("data.findAll{it.last_name}.last_name.flatten()",
                            hasItem("Edwards"));
        });
    }

    @Test
    public void successfulLogin() {
        step("Ввести валидный логин и пароль", () -> {
            SuccessfulLogin successLogin = new SuccessfulLogin();
            successLogin.setEmail("eve.holt@reqres.in");
            successLogin.setPassword("cityslicka");

            ResponseModel responseModel = given()
                    .spec(requestSpec)
                    .body(successLogin)
                    .when()
                    .post("/login")
                    .then()
                    .log().body()
                    .spec(responseSpec200)
                    .extract().as(ResponseModel.class);

            assertThat(responseModel.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        });
    }

    @Test
    public void successfulRegister() {
        step("Ввести валидный логин и пароль", () -> {
            SuccessfulLogin successLogin = new SuccessfulLogin();
            successLogin.setEmail("eve.holt@reqres.in");
            successLogin.setPassword("pistol");

                     given()
                    .spec(requestSpec)
                    .body(successLogin)
                    .when()
                    .post("/register")
                    .then()
                    .log().body()
                    .spec(responseSpec200)
                    .body("id", is(4));
        });
    }


}