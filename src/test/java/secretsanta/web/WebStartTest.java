package secretsanta.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import se.romram.client.RelaxClient;
import secretsanta.web.WebStart;

import java.io.IOException;

/**
 * Created by micke on 2015-11-16.
 */
public class WebStartTest {
    String[] args = null;
    RelaxClient client;

    @Before
    public void setup() {
        client = new RelaxClient();
        client.useDefaultCookieManager();
    }

    @Test
    public void testWebStart() throws IOException {
        WebStart.main(args);
        client.get("http://localhost:8088");
        assertThat(client.toString(), containsString("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/style.css\">"));
        assertThat(client.toString(), containsString("<input type=\"text\" name=\"username\""));
        assertThat(client.toString(), containsString("<input type=\"password\" name=\"password\""));
        // System.out.println(client.toString()); // Comment away when not needed!!
    }

}
