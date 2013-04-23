import org.junit.*;
import play.test.*;
import play.libs.*;

import models.*;
import notifiers.*;

public class MailTest extends UnitTest {

    @Test
    public void signUpEmail() throws Exception {
        String username = "bilbobaggins";
        String password =  "hello";
        String firstName = "Bilbo";
        String lastName = "Baggins";
        String email = "a@b.com";
        String admin = "admin";
        String phoneNumber = "123456789";
        String uuid ="32hre732r3jr";
        NdgUser user = new NdgUser( password, username, email,
                                    firstName, lastName,
                                    phoneNumber,
                                    admin, 'Y', 'Y', 'Y');
        Mails.sendActivationEmail(user,uuid);
        String emailer = Mail.Mock.getLastMessageReceivedBy("a@b.com");
        assertTrue(emailer.contains("Subject: Activate your account"));
    }
}