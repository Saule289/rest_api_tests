package rest;


import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static spec.Specification.*;

public class RestApiTests {

    @Tag("remote")
    @DisplayName("Получить данные пользователей со страницы 2")
    @Test
    void getListUsers() {
        step("Отправить get-запрос для получения данных со страницы 2", () -> {
            BodyResponseModel bodyResponseModel = given()
                    .spec(requestSpec)
                    .get("/users?page=2")
                    .then()
                    .spec(responseSpec200)
                    .extract().as(BodyResponseModel.class);

            assertThat(bodyResponseModel.getTotal()).isEqualTo(12);
            assertThat(bodyResponseModel.getPage()).isEqualTo(2);
        });
    }


    @Tag("remote")
    @DisplayName("Создание пользователя")
    @Test
    void createUsers() {
        step("Отправить post-запрос на создание пользования с body", () -> {
            CreateUserModel createUser = new CreateUserModel();
            createUser.setName("morpheus");
            createUser.setJob("leader");

            ResponseModel responseModel = given()
                    .spec(requestSpec)
                    .body(createUser)
                    .when()
                    .post("/users").then()
                    .spec(responseSpec201)
                    .extract().as(ResponseModel.class);

            assertThat(responseModel.getName()).isEqualTo("morpheus");
        });
    }

    @Tag("remote")
    @DisplayName("Обновить работу у созданного пользователя")
    @Test
    void updateUserInfo() {
        step("Обновить работу у созданного пользователя", () -> {
            CreateUserModel bodyModel = new CreateUserModel();
            bodyModel.setName("morpheus");
            bodyModel.setJob("zion resident");

            ResponseModel responseModel = given()
                    .spec(requestSpec)
                    .body(bodyModel)
                    .when()
                    .patch("/users/2")
                    .then()
                    .spec(responseSpec200)
                    .extract().as(ResponseModel.class);

            assertThat(responseModel.getJob()).isEqualTo("zion resident");
        });
    }

    @Tag("remote")
    @DisplayName("Удаление пользователя")
    @Test
    void deleteUsers() {
        step("Удалить пользователя с помощью delete запроса", () -> {
            given()
                    .spec(requestSpec)
                    .when()
                    .delete("https://reqres.in/api/users/2")
                    .then()
                    .spec(responseSpec204);
        });
    }

    @Tag("remote")
    @DisplayName("НЕуспешная регистрация из-за отсутствия пароля")
    @Test
    void unsuccessfulRegister() {
        step("Ввод email в поле для регистрации для неуспешной регистрации", () -> {
            UnsuccessfulModel unsuccessfulModel = new UnsuccessfulModel();
            unsuccessfulModel.setEmail("test@mail.ru");

           ResponseUnsuccessfulModel unsuccessfulResponse = given()
                    .spec(requestSpec)
                    .body(unsuccessfulModel)
                    .when()
                    .post("/register")
                    .then()
                    .spec(responseSpec400)
                    .extract().as(ResponseUnsuccessfulModel.class);

            assertThat(unsuccessfulResponse.getError()).isEqualTo("Missing password");
        });
    }

}

