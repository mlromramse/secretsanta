package secretsanta.web;

import se.romram.handler.RelaxHandler;
import se.romram.server.RelaxRequest;
import se.romram.server.RelaxResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by micke on 2015-11-15.
 */
public class WebHandler implements RelaxHandler {
    String HEADER = "<html><header><title>Secret Santa</title></header><body>";
    String FOOTER = "</body></html>";
    String LOGIN_FORM = "<form method=\"POST\" action=\"/\"><input name=\"username\"><input type=\"password\" name=\"password\"><button type=\"submit\">Logga in</button></form>";
    String LOGIN_PAGE = HEADER + LOGIN_FORM + FOOTER;

    @Override
    public boolean handle(RelaxRequest relaxRequest, RelaxResponse relaxResponse) {
        if (relaxRequest.getPath().indexOf('/', 1) != -1) {
            /* Release if specific file is requested. */
            return false;
        }
        relaxResponse.setContentType("text/html");
        Map cookieMap = getCookieMap(relaxRequest);
        if (cookieMap.containsKey("Session")) {
            /* You are logged in */
            showRecipient(relaxRequest, relaxResponse);
            return true;
        } else {
            /* You are not logged in */
            if (relaxRequest.getMethod().equalsIgnoreCase("POST")) {
                /* You have posted your login details */
                Map payloadMap = parsePayload(relaxRequest.getPayload());
                if (payloadMap.get("password").equals("pass")) {
                    /* You have the correct password */
                    relaxResponse.addHeaders("Set-Cookie:Session=hejsan");
                    showRecipient(relaxRequest, relaxResponse);
                    return true;
                }
            }
        }
        /* You need to log in */
        showLogin(relaxRequest, relaxResponse);
        return true;
    }

    private void showRecipient(RelaxRequest relaxRequest, RelaxResponse relaxResponse) {
        relaxResponse.respond(200, "tjosan");
    }

    private void showLogin(RelaxRequest relaxRequest, RelaxResponse relaxResponse) {
        relaxResponse.setContentType("text/html");
        relaxResponse.respond(200, LOGIN_PAGE);
    }

    private Map parsePayload(byte[] payload) {
        Map result = new HashMap();
        String[] payloadArr = new String(payload).split("&");
        for (String nameValue : payloadArr) {
            String[] nameValueArr = nameValue.split("=");
            result.put(nameValueArr[0], nameValueArr[1]);
        }
        return result;
    }

    private Map getCookieMap(RelaxRequest relaxRequest) {
        Map cookieMap = new HashMap();
        String cookieString = relaxRequest.getFromHeader("Cookie", null);
        if (cookieString != null) {
            String[] cookieArr = cookieString.split(";");
            for (String cookieItem : cookieArr) {
                String[] cookieSplit = cookieItem.split("=");
                cookieMap.put(cookieSplit[0].trim(), cookieSplit[1].trim());
            }
        }
        return cookieMap;
    }
}
