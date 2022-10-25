package coms.w4156.moviewishlist.models;

public interface ModelInterface<ID extends Comparable<ID>> {
    /**
     * Get the ID of the object.
     * @return The ID of the object
     */
    ID getId();
}
