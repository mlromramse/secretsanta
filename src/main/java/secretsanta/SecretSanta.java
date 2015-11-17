package secretsanta;

/**
 * Created by alicia on 15-11-15.
 */
public class SecretSanta {

    public static void main(String[] args) {

        Recipient kalle = new Recipient("Kalle");

        RecipientList recipientList = new RecipientList();
        recipientList.addRecipient(kalle);

        System.out.println(recipientList.getFormattedList("--- %s ---"));

    }
}
