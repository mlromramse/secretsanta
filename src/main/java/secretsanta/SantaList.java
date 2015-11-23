package secretsanta;

import java.util.ArrayList;

/**
 * Created by alicia on 15-11-15.
 */
public class SantaList {
    ArrayList<Santa> santas = new ArrayList<Santa>();

    public void addSanta(Santa santa) {
        santas.add(santa);
    }

    public String getFormattedList(String format) {
        String result = "";
        for (Santa santa : santas) {
            result += santa.getFormatted(format);
        }
        return result;
    }
}
