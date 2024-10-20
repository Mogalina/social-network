package repository;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import models.Entity;

/**
 * A generic repository interface for managing entities that extend the Entity class.
 *
 * This interface provides CRUD operations (Create, Read, Update, Delete) for entities identified by a unique ID of type
 * {@code ID}.
 *
 * @param <ID> the type of the unique identifier for the entity
 * @param <E>  the type of the entity that extends {@link Entity} and is managed by the repository
 */
public interface Repository<ID, E extends Entity<ID>> {

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return the entity with the specified ID, or {@code null} if no entity is found
     * @throws EntityNotFoundException if the entity doesn't exist in the system
     */
    E findOne(ID id) throws EntityNotFoundException;

    /**
     * Retrieves all entities managed by the repository.
     *
     * @return an iterable collection of all entities in the repository
     */
    Iterable<E> findAll();

    /**
     * Saves a new entity or updates an existing entity in the repository.
     *
     * @param entity the entity to be saved or updated
     * @return the saved entity (if the entity is new, it will return the entity with any changes that occurred during
     *         the save operation, such as auto-generated IDs)
     * @throws EntityAlreadyExistsException if entity already exists in the system
     */
    E save(E entity) throws EntityAlreadyExistsException;

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return the deleted entity, or {@code null} if no entity with the specified ID exists
     * @throws EntityNotFoundException if the entity doesn't exist in the system
     */
    E delete(ID id) throws EntityNotFoundException;

    /**
     * Updates an existing entity in the repository.
     *
     * @param entity the entity with updated data
     * @return the updated entity, or {@code null} if the entity does not exist in the repository
     */
    E update(E entity);
}
