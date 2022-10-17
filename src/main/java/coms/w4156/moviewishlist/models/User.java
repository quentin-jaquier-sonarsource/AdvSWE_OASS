package coms.w4156.moviewishlist.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;

    protected User() {}
    
    public User(@JsonProperty String username, @JsonProperty String password) {
        this.username = username;
        this.password = hashPassword(password);
    }

    @Override
    public String toString() {
        return String.format("User(username='%s')", username);
    }

    public String getUsername() {
        return this.username;
    }

    public Boolean checkPassword(String comparePassword) {
        return hashPassword(comparePassword).equals(hashPassword(this.password));
    }

    private String hashPassword(String plainPassword) {
        // We can use a hashing function here later
        // Or we can find a better way to do auth
        return plainPassword;
    }
}
