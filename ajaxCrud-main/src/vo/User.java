package vo;

public class User {
    private String username;
    private String password;
    private String chrName;
    private String email;
    private String provinceName;
    private String city;

    public User(String username, String chrName, String email, String provinceName, String city) {
        this.username = username;
        this.chrName = chrName;
        this.email = email;
        this.provinceName = provinceName;
        this.city = city;
    }

    public User(String username, String password, String chrName, String email, String provinceName, String city) {
        this.username = username;
        this.password = password;
        this.chrName = chrName;
        this.email = email;
        this.provinceName = provinceName;
        this.city = city;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", chrName='" + chrName + '\'' +
                ", email='" + email + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
