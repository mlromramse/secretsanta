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
    String HEADER = "<html>\n\t<header>\n\t\t<title>Secret Santa</title>" +
            "\n\t\t<link type=\"text/css\" rel=\"stylesheet\" href=\"css/style.css\">\n\t</header>\n\t<body>\n";
    /**
     * content as String
     */
    String CONTAINER_FORMAT = "\t\t<div class=\"%s\">\n%s\n\t\t</div> <!-- container -->\n"; //class, content
    /**
     * class as String then src as String
     */
    String IMG_FORMAT = "\t\t\t<img class=\"%s\" src=\"%s\">"; // class, src
    String FORM_FORMAT = "\t\t\t<form method=\"POST\" action=\"%s\">\n%s\n\t\t\t</form>"; //action, content
    String SELECT_FORMAT = "\t\t\t\t<select class=\"%s\" name=\"%s\">%s</select>"; //class, name, content
    String OPTION_FORMAT = "\t\t\t\t\t<option value=\"%s\">%s</option>"; //value, content
    String FOOTER = "\t</body>\n</html>\n\n";
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
            if ("POST".equalsIgnoreCase(relaxRequest.getMethod())) {
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
        StringBuffer buf = new StringBuffer();
        String santa = String.format(CONTAINER_FORMAT, "image", String.format(IMG_FORMAT, "santa", "img/secret-santa.png"));
        String options = String.format(OPTION_FORMAT, "None", "Make a selection");
        String select = String.format(SELECT_FORMAT, "select", "exclude", options);
        String form = String.format(FORM_FORMAT, "/", select);
        buf.append(HEADER);
        buf.append(String.format(CONTAINER_FORMAT, "container", santa + form));
        buf.append(FOOTER);
        relaxResponse.respond(200, buf.toString());
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
