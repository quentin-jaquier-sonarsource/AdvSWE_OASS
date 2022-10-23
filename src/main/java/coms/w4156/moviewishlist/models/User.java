package coms.w4156.moviewishlist.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class User implements ModelInterface<Long> {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String username;

    @ToString.Exclude
    @Getter
    @Setter
    private String encodedPassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists;
    
    public User(@JsonProperty String email, @JsonProperty String username, @JsonProperty String encodedPassword) {
        this.email = email;
        this.username = username;
        this.encodedPassword = encodedPassword;
    }

    public List<Long> getWishlistIds() {
        return this.wishlists.stream()
            .map(wishlist -> wishlist.getId())
            .toList();
    }
}
