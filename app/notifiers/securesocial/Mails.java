/**
 * Copyright 2011 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package notifiers.securesocial;

import play.Play;
import play.mvc.Mailer;
import play.mvc.Router;
import models.NdgUser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.mail.EmailAttachment;

/**
 * A helper class to send welcome emails to users that signed up using the
 * Username Password controller
 *
 * @see securesocial.provider.providers.UsernamePasswordProvider
 * @see controllers.securesocial.UsernamePasswordController 
 */
public class Mails extends Mailer {
    private static final String MAILER_SUBJECT = "views.email.mailer.subject";
    private static final String MAILER_FROM = "views.email.mailer.from";
    private static final String SCHEDULER_SUBJECT = "views.email.mailer.scheduler.subject";
    private static final String SCHEDULER_FROM = "views.email.mailer.scheduler.from";
    private static final String ATTACHMENT_DESCRIPTION = "views.email.mailer.attachment.description";
    private static final String USERNAME_PASSWORD_CONTROLLER_ACTIVATE = "Application.activate";
    private static final String UUID = "uuid";

    public static void sendActivationEmail(NdgUser user, String uuid) {
        setSubject( Play.configuration.getProperty(MAILER_SUBJECT));
        setFrom(Play.configuration.getProperty(MAILER_FROM));
        addRecipient(user.email);
        Map<String, Object> args = new HashMap<String, Object>();
        args.put(UUID, uuid);
        String activationUrl = Router.getFullUrl(USERNAME_PASSWORD_CONTROLLER_ACTIVATE, args);
        send(user, activationUrl);
    }

    public static void sendScheduledResults(String email, String path) {
        setSubject(Play.configuration.getProperty(SCHEDULER_SUBJECT));
        addRecipient(email);
        setFrom(Play.configuration.getProperty(SCHEDULER_FROM));
        EmailAttachment attachment = new EmailAttachment();
        attachment.setDescription(Play.configuration.getProperty(ATTACHMENT_DESCRIPTION));
        attachment.setPath(path);
        addAttachment(attachment);
        send();
    }
}
