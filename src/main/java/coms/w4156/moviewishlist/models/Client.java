package coms.w4156.moviewishlist.models;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "clients",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "email")
    }
)
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Client implements ModelInterface<Long> {

    /**
     * ID of the movie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    @Setter
    private Long id;

    /**
     * email of the client.
     */
    @Getter
    @Setter
    private String email;

    /**
     * The list of profiles serviced by this client.
     */
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @Setter
    private List<Profile> profiles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Set<Role> roles;

    /**
     * Create a new Client object
     * @param id - ID of the client
     * @param email - email of the client
     * @param profiles - The list of profiles serviced by this client
     * @param roles - The roles of the client for authorization purposes
     */
    public Client(
        final Long id,
        final String email,
        final List<Profile> profiles,
        final Set<Role> roles
    ) {
        this.id = id;
        this.email = email;
        this.profiles = profiles;
        this.roles = roles;
    }

    /**
     * Create a new Client object with the given email and roles.
     * @param email
     * @param roles
     */
    public Client(final String email, Set<Role> roles) {
        this.email = email;
        this.roles = roles;
    }

    /**
     * Get list of profiles under this client.
     *
     * @return List of profiles under this client
     */
    public List<Profile> getProfiles() {
        if (profiles == null) {
            return List.of();
        }
        return profiles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
