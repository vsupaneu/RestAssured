package newTask.swapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

/**1. Выполнить get https://swapi.co/api/planets/
 * 2. Из запроса достать url первой планеты
 * 3. Выполнить get полученного url
 * 4. Проверить что status code 200 */

public class TestTask2 {
    @Test
    public void Task1() {
        System.out.println("swapi - test 2 is running ");
        String response = get("https://swapi.co/api/planets/").asString();
        List<String> planetsList = from(response).getList("results*.url");

        String firstPlanetUrl = planetsList.get(0);

        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get(firstPlanetUrl)
                .then()
                .assertThat()
                .statusCode(200);
        // System.out.println(list);
    }
}
