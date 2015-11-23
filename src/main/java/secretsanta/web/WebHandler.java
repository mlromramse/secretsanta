package secretsanta.web;

import se.romram.handler.RelaxHandler;
import se.romram.server.RelaxRequest;
import se.romram.server.RelaxResponse;
import secretsanta.RecipientList;
import secretsanta.SantaList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by micke on 2015-11-15.
 */
public class WebHandler implements RelaxHandler {
    String HEADER = "<html>\n\t<header>\n\t\t<title>Secret Santa</title>\n" +
            "\t\t<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
            "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
            "\t\t<link type=\"text/css\" rel=\"stylesheet\" href=\"css/bootstrap.css\">\n" +
            "\t\t<link type=\"text/css\" rel=\"stylesheet\" href=\"css/bootstrap-responsive.css\">\n" +
            "\t\t<link type=\"text/css\" rel=\"stylesheet\" href=\"css/bootstrap-theme.min.css\">\n" +
            "\t\t<link type=\"text/css\" rel=\"stylesheet\" href=\"css/style.css\">\n" +
            "\t</header>\n\t<body>\n";
    /**
     * content as String
     */
    String CONTAINER_FORMAT = "\t\t<div class=\"%s\">\n%s\n\t\t</div> <!-- container -->\n"; //class, content
    /**
     * class as String then src as String
     */
    String IMG_FORMAT = "\t\t\t<img class=\"%s\" src=\"%s\">\n"; // class, src
    String FORM_FORMAT = "\t\t\t<form method=\"POST\" action=\"%s\">\n%s\n\t\t\t</form>\n\n"; //action, content
    String FIELDSET_FORMAT = "\t\t\t\t<fieldset>%s</fieldset>\n"; //content
    String LABEL_FORMAT = "\t\t\t\t<label for=\"%s\">%s</label>\n"; //for, text
    String INPUT_FORMAT = "\t\t\t\t<input type=\"%s\" name=\"%s\">\n"; //type, name
    String BUTTON_FORMAT = "\t\t\t\t<button class=\"%s\" name=\"%s\">%s</button>\n"; //class, name, text
    String SELECT_FORMAT = "\t\t\t\t<select class=\"%s\" name=\"%s\">%s</select>\n\n"; //class, name, content
    String OPTION_FORMAT = "\t\t\t\t\t<option value=\"%s\">%s</option>\n"; //value, content
    String FOOTER = "\t</body>\n<script src=\"js/bootstrap.min.js\"/><script src=\"js/jquery.min.js\"/></html>\n\n";
    String LOGIN_FORM = "<form method=\"POST\" action=\"/\"><input name=\"username\"><input type=\"password\" name=\"password\"><button type=\"submit\">Logga in</button></form>";
    String LOGIN_PAGE = HEADER + LOGIN_FORM + FOOTER;

    SantaList santaList;
    RecipientList recipientList;

    public WebHandler(SantaList santaList, RecipientList recipientList) {
        this.santaList = santaList;
        this.recipientList = recipientList;
    }

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
        StringBuffer options = new StringBuffer();
        options.append(String.format(OPTION_FORMAT, "None", "Make a selection if you want"));
        options.append(santaList.getFormattedList(OPTION_FORMAT));
        String selectLabel = String.format(LABEL_FORMAT, "select", "You can deselect one person.");
        String select = String.format(SELECT_FORMAT, "select", "exclude", options);
        String button = String.format(BUTTON_FORMAT, "btn btn-large btn-primary", "submit", "Get Your Name");
        String form = String.format(FORM_FORMAT, "/", selectLabel + select + button);
        buf.append(HEADER);
        buf.append(String.format(CONTAINER_FORMAT, "container", santa + form));
        buf.append(FOOTER);
        relaxResponse.respond(200, buf.toString());
    }

    private void showLogin(RelaxRequest relaxRequest, RelaxResponse relaxResponse) {
        StringBuffer buf = new StringBuffer();
        String santa = String.format(CONTAINER_FORMAT, "image", String.format(IMG_FORMAT, "santa", "img/secret-santa.png"));
        StringBuffer options = new StringBuffer();
        options.append(String.format(OPTION_FORMAT, "None", "Who are you?"));
        options.append(santaList.getFormattedList(OPTION_FORMAT));
        String select = String.format(SELECT_FORMAT,"username","username", options);
        String password = String.format(INPUT_FORMAT, "password", "password");
        String button = String.format(BUTTON_FORMAT, "btn btn-large btn-primary", "submit", "Log in");
        String fieldset = String.format(FIELDSET_FORMAT, select + password + button);
        String form = String.format(FORM_FORMAT, "/", fieldset);
        buf.append(HEADER);
        buf.append(String.format(CONTAINER_FORMAT, "container", santa + form));
        buf.append(FOOTER);
        relaxResponse.respond(200, buf.toString());
    }

    private Map parsePayload(byte[] payload) {
        System.out.println(new String(payload));
        Map result = new HashMap();
        String[] payloadArr = new String(payload).split("&");
        for (String nameValue : payloadArr) {
            String[] nameValueArr = nameValue.split("=");
            if (nameValueArr.length == 2) {
                result.put(nameValueArr[0], nameValueArr[1]);
            }
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
                if (cookieSplit.length == 2) {
                    cookieMap.put(cookieSplit[0].trim(), cookieSplit[1].trim());
                }
            }
        }
        return cookieMap;
    }
}
