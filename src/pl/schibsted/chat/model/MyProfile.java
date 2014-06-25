package pl.schibsted.chat.model;

/**
 * @author krzysztof.kosobudzki
 */
public class MyProfile {
//    @SerializedName("firstname")
    private String firstName;
//    @SerializedName("lastname")
    private String lastName;

    public MyProfile() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
