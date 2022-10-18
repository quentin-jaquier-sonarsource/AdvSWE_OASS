package coms.w4156.moviewishlist.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "users")
@ToString
public class User implements ModelInterface<String> {
    @Id
    @Getter
    private String username;

    @ToString.Exclude
    private String password;

    protected User() {}
    
    public User(@JsonProperty String username, @JsonProperty String password) {
        this.username = username;
        this.password = hashPassword(password);
    }

    public Boolean checkPassword(String comparePassword) {
        return hashPassword(comparePassword).equals(hashPassword(this.password));
    }

    @Override
    public String getId() {
        return this.getUsername();
    }

    public User setPassword(String newPassword) {
        this.password = hashPassword(newPassword);
        return this;
    }

    private String hashPassword(String plainPassword) {
        // We can use a hashing function here later
        // Or we can find a better way to do auth
        return plainPassword;
    }
}
