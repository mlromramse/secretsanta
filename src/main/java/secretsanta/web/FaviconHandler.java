package secretsanta.web;

import se.romram.handler.RelaxHandler;
import se.romram.server.RelaxRequest;
import se.romram.server.RelaxResponse;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by micke on 2015-11-16.
 */
public class FaviconHandler implements RelaxHandler {
    private String pathAsString;
    private Path path;

    public FaviconHandler(String pathAsString) {
        path = FileSystems.getDefault().getPath(pathAsString);
        this.pathAsString = pathAsString;
    }

    @Override
    public boolean handle(RelaxRequest relaxRequest, RelaxResponse relaxResponse) {
        if ("/favicon.ico".equalsIgnoreCase(relaxRequest.getPath())) {
            try {
                byte[] payload = Files.readAllBytes(path);
                relaxResponse.setContentType("image/x-icon");
                relaxResponse.respond(200, payload);
                return true;
            } catch (IOException e) {
                relaxResponse.respond(404, "Resource is missing.");
                return true;
            }
        }
        return false;
    }
}
