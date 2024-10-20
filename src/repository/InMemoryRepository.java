package repository;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import models.Entity;
import models.validators.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * A generic in-memory (local) repository for managing entities.
 * This class provides CRUD (Create, Read, Update, Delete) operations on entities stored in memory (locally).
 *
 * @param <ID> the type of the entity's identifier
 * @param <E> the type of the entity, which must extend {@link Entity<ID>}
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator; // Validator for entity
    protected Map<ID, E> entities; // Storage for entities, indexed by their identifier

    /**
     * Constructs a new InMemoryRepository with the specified validator.
     *
     * @param validator the validator used to validate the entities
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.entities = new HashMap<ID, E>();
    }

    /**
     * Find an entity by its identifier.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return the entity with the specified
     * @throws EntityNotFoundException if the entity doesn't exist in the system
     * @throws NullPointerException if the provided identifier is null
     */
    @Override
    public E findOne(ID id) throws EntityNotFoundException {
        if (id == null) {
            throw new NullPointerException("ID must not be null");
        }

        E entity = entities.get(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }

        return entity;
    }

    /**
     * Retrieves all entities in the repository (storage).
     *
     * @return an iterable collection of all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * Saves a new entity in the repository (storage).
     *
     * @param entity the entity to be saved
     * @return the saved entity
     * @throws EntityAlreadyExistsException if the entity already exists in the system
     * @throws NullPointerException if the provided entity is null
     */
    @Override
    public E save(E entity) throws EntityAlreadyExistsException {
        if (entity == null) {
            throw new NullPointerException("Entity must not be null");
        }

        ID id = entity.getId();
        if (entities.get(id) != null) {
            throw new EntityAlreadyExistsException();
        }

        validator.validate(entity);
        entities.put(id, entity);
        return entity;
    }

    /**
     * Deletes an entity from the repository (storage) by its identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return the deleted entity
     * @throws EntityNotFoundException if the entity doesn't exist in the system
     * @throws NullPointerException if the provided identifier is null
     */
    @Override
    public E delete(ID id) throws EntityNotFoundException {
        if (id == null) {
            throw new NullPointerException("ID must not be null");
        }

        E entity = entities.remove(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        return entity;
    }

    /**
     * Updates an existing entity in the repository (storage).
     *
     * @param entity the entity to be updated
     * @return the updated entity
     * @throws NullPointerException if the provided entity is null
     */
    @Override
    public E update(E entity) {
        if (entity == null) {
            throw new NullPointerException("Entity must not be null");
        }

        validator.validate(entity);
        entities.put(entity.getId(), entity);
        return entity;
    }
}
