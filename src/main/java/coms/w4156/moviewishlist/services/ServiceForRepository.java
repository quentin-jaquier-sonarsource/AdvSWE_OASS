package coms.w4156.moviewishlist.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import coms.w4156.moviewishlist.models.ModelInterface;

public abstract class ServiceForRepository<
        ID extends Comparable<ID>,
        Model extends ModelInterface<ID>,
        Repository extends CrudRepository<Model, ID>
> {
    protected Repository repository;
    
    public List<Model> getAll() {
        return StreamSupport
                .stream(this.repository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Model::getId))
                .collect(Collectors.toList());
    }

    public Optional<Model> findById(ID id) {
        return this.repository.findById(id);
    }
    
    public Model create(Model object) {
        return this.repository.save(object);
    }

    public Model update(Model object) {
        return this.repository.save(object);
    }

    // Add methods for updating individual properties

    public void deleteAll() {
        this.repository.deleteAll();
    }

    public Optional<Model> deleteById(ID id) {
        Optional<Model> wishlist = this.repository.findById(id);

        if (wishlist.isPresent()) {
            this.repository.deleteById(id);
        }

        return wishlist;
    }

}
