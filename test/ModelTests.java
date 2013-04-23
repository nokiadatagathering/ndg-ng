import org.junit.*;
import play.test.*;

import models.*;

public class ModelTests extends UnitTest {
    
    @Before
    public void setUpData() {
        //Fixtures.deleteAll();
        Fixtures.load("data.yml");
    }


    @Test
    public void countObjects() {
        assertEquals(1, NdgUser.count());
        assertEquals(6, Question.count());
    }
}