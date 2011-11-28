package controllers.logic;

import controllers.util.Constants;
import java.util.Calendar;
import models.NdgUser;
import play.libs.Codec;
import play.mvc.Http;
import play.mvc.Http.Response;
import play.mvc.Http.StatusCode;
import play.mvc.Scope.Session;

public class AuthorizationUtils {
    public static boolean isAuthorized(Http.Header authorizationHeader, String method)
    {
        boolean retval = false;
        if(authorizationHeader != null) {

            String authorizationString = authorizationHeader.value().replace(',', ' ');
            String username = getParamValue("username", authorizationString);
            NdgUser user = NdgUser.find("byUsername", username).first();
            if(user != null) {
            String receivedDigest = getParamValue("response", authorizationString);
            String serverDigest = generateDigest(user.password,
                                                 getParamValue(" nonce", authorizationString),
                                                 getParamValue(" uri", authorizationString),
                                                 getParamValue(" qop", authorizationString),
                                                 getParamValue(" nc", authorizationString),
                                                 getParamValue(" cnonce", authorizationString),
                                                 method);
            retval = receivedDigest.equals(serverDigest);
            }
        }
        return retval;
    }

    private static String getParamValue(String string, String value) {
        int startIndex = value.indexOf(string) + string.length();
        int endIndex = startIndex;
        boolean quoted = (value.charAt(startIndex + 1) == '"');
        if(quoted) {
            startIndex += 2;
            endIndex = value.indexOf("\"", startIndex);
        } else {
            startIndex += 1;
            endIndex = value.indexOf(" ", startIndex);
        }
        return value.substring( startIndex, endIndex );
    }

    private static String generateDigest(String password, String nonce, String uri, String qop, String nc, String cnonce, String method) {
        StringBuilder combinedH1H2 = new StringBuilder();
        StringBuilder H2 = new StringBuilder(method);
        H2.append(":").append(uri);
        combinedH1H2.append(password).
                     append(":").append(nonce).
                     append(":").append(nc).
                     append(":").append(cnonce).
                     append(":").append(qop).
                     append(":").append(Codec.hexMD5(H2.toString()));
        return Codec.hexMD5(combinedH1H2.toString());
    }

    public static void setDigestResponse(Response response)
    {
        String digest = AuthorizationUtils.generateDigest();
        response.setHeader("WWW-Authenticate", digest);
        response.setHeader("X-WWW-Authenticate", digest.substring(digest.indexOf(' ') + 1));

        response.status = StatusCode.UNAUTHORIZED;
    }

    private static String generateDigest() {
        StringBuilder authHeader = new StringBuilder();
        authHeader.append("Digest realm=\"NDG\", qop=\"auth\", nonce=\"");
        authHeader.append(Calendar.getInstance().getTimeInMillis());
        authHeader.append(Constants.SERVER_KEY);
        authHeader.append("\", opaque=\"bmRnb3BhcXVl\"");
        return authHeader.toString();
    }

    public static NdgUser extractUserFromHeader(Http.Header authorizationHeader) {
            String authorizationString = authorizationHeader.value().replace(',', ' ');
            String username = getParamValue("username", authorizationString);
            return NdgUser.find("byUsername", username).first();
    }

    public static boolean checkWebAuthorization(Session session, Response response) {
        return checkWebAuthorization(session, response, false);
    }

    public static boolean checkWebAuthorization(Session session, Response response, boolean adminRestricted) {
        boolean retval = true;
        if (!session.contains("ndgUser") || ( adminRestricted && !sessionHasAdmin(session))) {
            response.status = response.status = StatusCode.UNAUTHORIZED;
            retval = false;
        }
        return retval;
    }

    private static boolean sessionHasAdmin(Session session)
    {
        return session.contains("admin") && session.get("admin").equals("true");
    }

}