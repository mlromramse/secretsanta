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
    WebStart webStart;

    @Before
    public void setup() {

    }

    @Test
    public void testWebStart() throws IOException {
        String[] args = null;
        WebStart.main(args);
        RelaxClient client = new RelaxClient();
        client.get("http://localhost:8088");
        assertThat(client.toString(), containsString("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/style.css\">"));
        assertThat(client.toString(), containsString("<input name=\"username\""));
        assertThat(client.toString(), containsString("<input type=\"password\" name=\"password\""));
        // System.out.println(client.toString()); // Comment away when not needed!!
    }

}
