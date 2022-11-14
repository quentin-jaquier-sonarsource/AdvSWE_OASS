package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.ModelInterface;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.CrudRepository;

/**
 * An abstract class that implements the methods for all services.
 *
 * @param <ID> - The ID type of the model
 * @param <Model> - The type of the model served
 * @param <Repository> - The type of the Repository storing this model
 */
public abstract class ServiceForRepository<
    ID extends Comparable<ID>,
    Model extends ModelInterface<ID>,
    Repository extends CrudRepository<Model, ID>
> {

    /**
     * The repository that stores the data for given model.
     * This reposity will be Autowired in the subclasses.
     */
    @Getter
    @Setter
    private Repository repository;

    /**
     * Get all the models from the repository.
     *
     * @return List of model objects
     */
    public List<Model> getAll() {
        return StreamSupport
            .stream(this.repository.findAll().spliterator(), false)
            .sorted(Comparator.comparing(Model::getId))
            .collect(Collectors.toList());
    }

    /**
     * Get a particular object from the repository by ID.
     *
     * @param id - The ID of the object to get
     * @return The Obect that was found, or None
     */
    public Optional<Model> findById(final ID id) {
        return this.repository.findById(id);
    }

    /**
     * Add a new Model object to the repository.
     *
     * @param object - The object to add to the repository
     * @return - The object that was just added to the repository
     */
    public Model create(final Model object) {
        return this.repository.save(object);
    }

    /**
     * Update an object in the repository.
     *
     * @param object - The updated object
     * @return - The updated object
     */
    public Model update(final Model object) {
        return this.repository.save(object);
    }

    // Methods for updating individual properties.

    /**
     * Delete all objects in the repository.
     */
    public void deleteAll() {
        this.repository.deleteAll();
    }

    /**
     * Delete an object that matches the given ID.
     *
     * @param id - The ID of the model to delete
     * @return - The model that was just deleted
     */
    public Optional<Model> deleteById(final ID id) {
        Optional<Model> wishlist = this.repository.findById(id);

        if (wishlist.isPresent()) {
            this.repository.deleteById(id);
        }

        return wishlist;
    }
}
