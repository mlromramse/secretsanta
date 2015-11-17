package secretsanta;

import java.util.Random;

/**
 * Created by alicia on 15-11-15.
 */
public class Helper {

    static String getId() {
        return "" + System.currentTimeMillis();
    }

    static String getPassword() {
        Random random = new Random();
        return "" + random.nextInt(9000) + 1000;
    }
}
