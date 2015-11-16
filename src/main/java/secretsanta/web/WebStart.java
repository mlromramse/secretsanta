package secretsanta.web;

import se.romram.handler.DefaultFileHandler;
import se.romram.server.RelaxServer;

import java.io.IOException;

/**
 * Created by micke on 2015-11-15.
 */
public class WebStart {

    public static void main(String[] args) throws IOException {
        RelaxServer server = new RelaxServer(8088, new FaviconHandler("webroot/img/favicon.ico"));
        server.addRelaxHandler(new WebHandler());
        server.addRelaxHandler(new DefaultFileHandler("webroot"));
        server.start();
    }
}
