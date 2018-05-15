package mikekamau.com.openchat.entities;

public class User {
    private String id;
    private String name;
    private String profilePicUrlString;

    public User() {
    }

    public User(String id, String name, String profilePicUrlString) {
        this.id = id;
        this.name = name;
        this.profilePicUrlString = profilePicUrlString;
    }

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

    public String getProfilePicUrlString() {
        return profilePicUrlString;
    }

    public void setProfilePicUrlString(String profilePicUrlString) {
        this.profilePicUrlString = profilePicUrlString;
    }
}
