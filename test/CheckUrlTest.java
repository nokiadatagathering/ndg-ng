import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
//import models.*;

public class CheckUrlTest extends FunctionalTest {

    @Test
    public void testThatCheckUrlPageWorks() {
        Response response = GET("/CheckUrl");
        assertIsOk(response);
        assertContentType("text/plain", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

}
