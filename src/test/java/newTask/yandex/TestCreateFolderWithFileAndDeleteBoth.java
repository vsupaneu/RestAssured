package newTask.yandex;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

public class TestCreateFolderWithFileAndDeleteBoth {

    /**
     * 1. Создать папку на яндекс диске
     * 2. Внутри созданной папки создать файл
     * 3. Удалить созданный файл
     * 4. Удалить созданную папку
     */

    @Test
    public void TestCreateFolder() {
        System.out.println("yandex - test 2 is running ");
        //create folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "disk:/newFolder3")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(201);

        //get link to upload file
        String href =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .queryParam("path", "disk:/newFolder3/batman.jpg")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/resources/upload")
                        .then()
                        .statusCode(200)
                        .extract().path("href");

        //upload file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder3")
                .when()
                .put(href)
                .then()
                .statusCode(201);

        //delete file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder3/batman.jpg")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

        //delete folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder3")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

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
