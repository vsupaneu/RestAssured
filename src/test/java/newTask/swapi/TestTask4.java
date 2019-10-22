package newTask.swapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.Random;

/**1. авторизоваться(ввести люой токен) https://openapi.appcenter.ms/#:
 * http://prntscr.com/js2kgp
 * http://prntscr.com/js2ktl
 * 2. Использовать логин для любого метода post и выполнить post
 * пример https://api.appcenter.ms/v0.1/user/invitations/orgs/arsenal/reject */

public class TestTask4 {
    @Test
    public void TestRequestForOpenAPI() {
        Random random = new Random();
        int rand = random.nextInt(10000);
        System.out.println("swapi - test 4 is running ");
        String requestBody = "{  \"display_name\": \"TestOrganization" + rand + "\",  \"name\": \"TestOrganization" + rand + "\"}";

        //create new organization
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("X-API-Token", "936bf978fe88c76ee882cbc780f51f84319b6b35")
                .body(requestBody)
                .post("https://api.appcenter.ms/v0.1/orgs")
                .then()
                .statusCode(201);

        //  выполнить post пример https://api.appcenter.ms/v0.1/user/invitations/orgs/arsenal/reject
        //  result should be - "No organization invitation exists with token \"arsenal\"
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("X-API-Token", "936bf978fe88c76ee882cbc780f51f84319b6b35")
                .post("https://api.appcenter.ms/v0.1/user/invitations/orgs/arsenal/reject")
                .then()
                .statusCode(404);
    }
}


