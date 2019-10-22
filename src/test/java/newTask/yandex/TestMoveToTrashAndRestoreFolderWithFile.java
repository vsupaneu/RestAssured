package newTask.yandex;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

/**1. Создать папку на яндекс диске
 * 2. Внутри созданной папки создать файл
 * 3. Поместить созданный файл в корзину
 * 4. Восстановить созданный файл из корзины
 * 5. Удалить файл и папку */

public class TestMoveToTrashAndRestoreFolderWithFile {
    @Test
    public void TestMoveToTrashAndRestoreFOlderWithFile() {
        System.out.println("yandex - test 3 is running ");
        //Create folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "disk:/newFolder4")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(201);

        //Get link
        String href =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .queryParam("path", "disk:/newFolder4/batman.jpg")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/resources/upload")
                        .then()
                        .statusCode(200)
                        .extract().path("href");

        //Upload file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder4")
                .when()
                .put(href)
                .then()
                .statusCode(201);

        //delete file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder4/batman.jpg")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

        //ensure file in trash
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "batman.jpg")
                .when()
                .get("https://cloud-api.yandex.net/v1/disk/trash/resources/")
                .then()
                .statusCode(200);

        //restore file from trash
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "batman.jpg")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/trash/resources/restore")
                .then()
                .statusCode(201);

        //ensure file restored
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "disk:/newFolder4/batman.jpg")
                .when()
                .get("https://cloud-api.yandex.net/v1/disk/resources")
                .then()
                .statusCode(200);

        //delete file again
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder4/batman.jpg")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

        //delete folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder4")
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
