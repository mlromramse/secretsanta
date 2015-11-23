package secretsanta.web;

import se.romram.handler.DefaultFileHandler;
import se.romram.main.Properties;
import se.romram.server.RelaxServer;
import secretsanta.Recipient;
import secretsanta.RecipientList;
import secretsanta.Santa;
import secretsanta.SantaList;

import java.io.IOException;

/**
 * Created by micke on 2015-11-15.
 */
public class WebStart {

    public static void main(String[] args) throws IOException {
        RecipientList recipientList = new RecipientList();
        
        recipientList.addRecipient(new Recipient("Alicia"));
        recipientList.addRecipient(new Recipient("Jennifer"));
        recipientList.addRecipient(new Recipient("Calolm"));
        recipientList.addRecipient(new Recipient("Catharina"));
        recipientList.addRecipient(new Recipient("Sylve"));
        recipientList.addRecipient(new Recipient("Hans"));
        recipientList.addRecipient(new Recipient("Helena"));
        recipientList.addRecipient(new Recipient("Mikael"));
        
        SantaList santaList = new SantaList();

        santaList.addSanta(new Santa("Alicia"));
        santaList.addSanta(new Santa("Jennifer"));
        santaList.addSanta(new Santa("Calolm"));
        santaList.addSanta(new Santa("Catharina"));
        santaList.addSanta(new Santa("Sylve"));
        santaList.addSanta(new Santa("Hans"));
        santaList.addSanta(new Santa("Helena"));
        santaList.addSanta(new Santa("Mikael"));
        
        RelaxServer server = new RelaxServer(8088, new FaviconHandler("webroot/img/favicon.ico"));
        server.addRelaxHandler(new WebHandler(santaList,recipientList));
        server.addRelaxHandler(new DefaultFileHandler("webroot"));
        server.start();
    }
}
