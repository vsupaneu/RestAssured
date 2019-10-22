package newTask.yandex;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Test;

public class TestCreateAndDeleteFolder {

    /**
     * 1. Создать папку на диске
     * 2. Удалить созданную папку
     * 3. Проверить, что папку удалена */

    @Test
    public void TestCreateFolder() {
        System.out.println("yandex - test 1 is running ");
        //create folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder2")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(201);

        //ensure folder is created
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder2")
                .when()
                .get("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(200);

        //delete folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder2")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

        //ensure folder is deleted
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder2")
                .when()
                .get("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(404);

        //clear trash
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/trash/resources/")
                .then()
                .statusCode(202);
    }
}