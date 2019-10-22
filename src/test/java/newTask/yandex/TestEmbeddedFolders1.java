package newTask.yandex;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

/**1. Создать папку test
 * 2. Внутри test создать папку foo
 * 3. Внутри foo создать файл autotest
 * 4. Получить метаданные test и сравнить что и тип параметров соответсвует ожидаемому
 * 5. Удалить папку тест
 * 6. Проверить ,что удалилась папку test, foo и файл autotest */

public class TestEmbeddedFolders1 {
    @Test
    public void TestEmbeddedFolders1() {
        System.out.println("yandex - test 5 is running ");
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

        //get info of test folder
        String response =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .queryParam("path", "test")
                        .expect()
                        .body("_embedded.total", equalTo(1))
                        .body("_embedded.path", equalTo("disk:/test"))
                        .body("name", equalTo("test"))
                        .statusCode(200)
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/resources/")
                        .then().extract().asString();

        //delete test folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "test")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(202);

        //ensure all entities are in trash
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "test")
                .when()
                .get("https://cloud-api.yandex.net/v1/disk/trash/resources/")
                .then()
                .statusCode(200);

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
