package coms.w4156.moviewishlist.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class User implements ModelInterface<String> {
    @Id
    @Getter
    private String email;

    @Getter
    @Setter
    private String name;

    @ToString.Exclude
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists;
    
    public User(@JsonProperty String email, @JsonProperty String name, @JsonProperty String password) {
        this.email = email;
        this.name = name;
        this.password = hashPassword(password);
    }

    public User(String email, String name, String password, List<Wishlist> wishlists) {
        this.email = email;
        this.name = name;
        this.password = hashPassword(password);
        this.wishlists = wishlists;
    }

    public Boolean checkPassword(String comparePassword) {
        return hashPassword(comparePassword).equals(hashPassword(this.password));
    }

    @Override
    public String getId() {
        return this.getEmail();
    }

    public List<Long> getWishlistIds() {
        return this.wishlists.stream()
            .map(wishlist -> wishlist.getId())
            .toList();
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
