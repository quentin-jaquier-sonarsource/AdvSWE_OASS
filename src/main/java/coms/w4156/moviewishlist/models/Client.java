package coms.w4156.moviewishlist.models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "clients")
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
    @Getter
    @Setter
    private List<Profile> profiles;

    /**
     * Create a new Client object.
     * @param id - ID of the client
     * @param email - email of the client
     * @param profiles - The list of profiles serviced by this client
     */
    public Client(final Long id, final String email, final List<Profile> profiles) {
        this.id = id;
        this.email = email;
        this.profiles = profiles;
    }

    public Client(final String email) {
        this.email = email;
    }
}
