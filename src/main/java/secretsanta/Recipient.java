package secretsanta;

/**
 * Created by alicia on 15-11-15.
 */
public class Recipient {
    String name;
    String id;
    Santa santa;

    public Recipient(String name){
        setName(name);
        setId(Helper.getId());
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormatted(String format) {
        return String.format(format, name);
    }
}
