package newTask;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import newTask.swapi.TestTask1;
import newTask.swapi.TestTask2;
import newTask.swapi.TestTask3;
import newTask.swapi.TestTask4;
import newTask.yandex.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestTask1.class,
        TestTask2.class,
        TestTask3.class,
        TestTask4.class,
        TestCreateAndDeleteFolder.class,
        TestCreateFolderWithFileAndDeleteBoth.class,
        TestEmbeddedFolders1.class,
        TestEmbeddedFolders2.class,
        TestGetInfoAboutUserDisk.class,
        TestMoveToTrashAndRestoreFolderWithFile.class
})

public class RunAll {
}
