package coms.w4156.moviewishlist.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    private Long id;
    private String name;
    private Long userId;

    protected Wishlist() {}

    public Wishlist(@JsonProperty String name, @JsonProperty Long userId) {
        this.name = name;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format(
            "Wishlist[id=%d, name='%s', userId=%d]",
            id, name, userId
        );
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}


