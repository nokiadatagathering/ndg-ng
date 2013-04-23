import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

import org.junit.*;

import play.test.*;

public class CheckAuth extends FunctionalTest {

    @Test
    public void indexIsFree() {
        assertStatus(302, GET("/"));
    }

    @Test
    public void resultPostIsRestricted() {
        assertStatus(401, POST("/PostResults"));
    }

    @Test
    public void receiveSurveyIsRestricted() {
        assertStatus(401, GET("/ReceiveSurvey"));
    }
}