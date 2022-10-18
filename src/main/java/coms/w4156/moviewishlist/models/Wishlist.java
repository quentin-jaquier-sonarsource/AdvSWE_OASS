package coms.w4156.moviewishlist.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wishlists")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
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
}


