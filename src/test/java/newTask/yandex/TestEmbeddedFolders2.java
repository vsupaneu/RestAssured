package newTask.yandex;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

/**1. Создать папку test
 * 2. Внутри test создать папку foo
 * 3. Внутри foo создать файл autotest
 * 4. Поместить test в корзину
 * 5. Очистить корзину
 * 6. Проверить, что корзина очищена и папоу и файлов нет */

public class TestEmbeddedFolders2 {
    @Test
    public void TestEmbeddedFolders2() {
        System.out.println("yandex - test 6 is running ");
        //create first folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "test")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(201);

        //create second folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "test/foo")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(201);

        //Get link
        String href =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .queryParam("path", "disk:/test/foo/batman.jpg")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/resources/upload")
                        .then()
                        .statusCode(200)
                        .extract().path("href");

        //Upload file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "test/foo")
                .when()
                .put(href)
                .then()
                .statusCode(201);

        //move test to trash
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "test")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(202);

        //clear trash
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/trash/resources/")
                .then()
                .statusCode(202);

        //ensure trash is empty
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "test")
                .when()
                .get("https://cloud-api.yandex.net/v1/disk/trash/resources/")
                .then()
                .statusCode(404);
    }
}
