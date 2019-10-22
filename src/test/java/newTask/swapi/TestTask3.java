package newTask.swapi;

import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.path.json.JsonPath.from;

/**1. Выполнить get https://swapi.co/api/films/
 *  2. Из запроса достать названия фильмов titles */

public class TestTask3 {
    @Test
    public void getFilmTitles() {
        System.out.println("swapi - test 3 is running ");
        String response = get("https://swapi.co/api/films/").asString();
        List<String> filmTitles = from(response).getList("results*.title");
    }
}
