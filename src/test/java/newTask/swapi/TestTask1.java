package newTask.swapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

/**1. Выполнить get https://swapi.co/api/planets/
 * 2. Проверить, что поле count равно 61 */

public class TestTask1 {
    @Test
    public void Task1() {
        System.out.println("swapi - test 1 is running ");
        RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                .get("https://swapi.co/api/planets/")
                .then()
                .assertThat()
                .body("count", equalTo(61));
    }
}
