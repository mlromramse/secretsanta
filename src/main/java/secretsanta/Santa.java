package secretsanta;

/**
 * Created by alicia on 15-11-15.
 */
public class Santa {
    String name;
    String id;
    String password;
    Recipient recipient;

    public Santa(String name) {
        setName(name);
        setId(Helper.getId());
        setPassword(Helper.getPassword());
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormatted(String format) {
        return String.format(format, name, name);
    }

}
