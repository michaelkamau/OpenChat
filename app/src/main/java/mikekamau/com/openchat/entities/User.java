package mikekamau.com.openchat.entities;

public class User {
    private String id;
    private String name;
    private String profileUrlString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrlString() {
        return profileUrlString;
    }

    public void setProfileUrlString(String profileUrlString) {
        this.profileUrlString = profileUrlString;
    }
}