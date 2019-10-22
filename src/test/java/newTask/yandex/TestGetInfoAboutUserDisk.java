package newTask.yandex;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**Получить информацию и диске пользователя
 * 1. Создать папку на яндекс диске
 * 2. Внутри созданной папки создать несколько файлов
 * 3. Поместить созданные файлы в корзину
 * 4. Посчитать размер файлов в корзине
 * 5. Сравнить что файлов в корзине = первоначальному разрамеру + размеру файлов в корзине
 * 4. Восстановить созданный файл из корзины
 * 5. Удалить файл и папку */

public class TestGetInfoAboutUserDisk {
    @Test
    public void TestGetInfoAboutUserDisk() {
        System.out.println("yandex - test 4 is running ");

        //get initial trash size
        int initialTrashSize =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/")
                        .then()
                        .extract().path("trash_size");

        //Create folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "disk:/newFolder5")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(201);

        //Get link for the first file
        String href1 =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .queryParam("path", "disk:/newFolder5/batman.jpg")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/resources/upload")
                        .then()
                        .statusCode(200)
                        .extract().path("href");

        //Upload first file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder5")
                .when()
                .put(href1)
                .then()
                .statusCode(201);

        //Get link for the second file
        String href2 =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .queryParam("path", "disk:/newFolder5/spider-man.jpg")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/resources/upload")
                        .then()
                        .statusCode(200)
                        .extract().path("href");

        //Upload second file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder5")
                .when()
                .put(href2)
                .then()
                .statusCode(201);

        //delete first file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder5/batman.jpg")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

        //delete second file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder5/spider-man.jpg")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

        //get trash content
        String response =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/trash/resources/")
                        .then().extract().response().asString();

        //count sizes of deleted files
        int fileSize1 = -1;
        int fileSize2 = -1;
        JSONObject object1 = new JSONObject(response);
        JSONObject object2 = object1.getJSONObject("_embedded");
        JSONArray array = (JSONArray) object2.get("items");

        for (int i = 0; i < array.length(); i++) {
            String s = array.getJSONObject(i).getString("name");
            if (s.equals("batman.jpg"))
                fileSize1 = array.getJSONObject(i).getInt("size");
            if (s.equals("spider-man.jpg"))
                fileSize2 = array.getJSONObject(i).getInt("size");
        }

        //get final trash size
        int finalTrashSize =
                RestAssured.given()
                        .accept(ContentType.JSON)
                        .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                        .when()
                        .get("https://cloud-api.yandex.net/v1/disk/")
                        .then()
                        .extract().path("trash_size");

        // ensure final trash size equals initial size + deleted files
        assertEquals(finalTrashSize, initialTrashSize + fileSize1 + fileSize2);

        //restore file from trash
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "batman.jpg")
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/trash/resources/restore/")
                .then()
                .statusCode(201);

        //delete restored file
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder5/batman.jpg")
                .when()
                .delete("https://cloud-api.yandex.net/v1/disk/resources/")
                .then()
                .statusCode(204);

        //delete folder
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization", "AgAAAAA5hnlMAAXs65Vz2MzJTUVTin03clj-WsE")
                .queryParam("path", "newFolder5")
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
