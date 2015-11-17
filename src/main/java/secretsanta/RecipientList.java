package secretsanta;

import java.util.ArrayList;

/**
 * Created by alicia on 15-11-15.
 */
public class RecipientList {
    ArrayList<Recipient> recipients= new ArrayList<Recipient>();

    public void addRecipient( Recipient recipient) {
        recipients.add(recipient);
    }

    public String getFormattedList(String format) {
        String result = "";
        for (Recipient recipient : recipients) {
            result += recipient.getFormatted(format);
        }
        return result;
    }
}
