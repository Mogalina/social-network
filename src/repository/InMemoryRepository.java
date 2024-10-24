package repository;

import exceptions.EntityAlreadyExistsException;
import exceptions.EntityNotFoundException;
import models.Entity;
import models.validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        this.entities = new HashMap<>();
    }

    /**
     * Find an entity by its identifier.
     *
     * @param id the unique identifier of the entity to be retrieved
     * @return an {@link Optional} containing the entity with the specified ID, or an empty {@code Optional} if no
     *         entity is found
     * @throws NullPointerException if the provided identifier is null
     */
    @Override
    public Optional<E> findOne(ID id) {
        if (id == null) {
            throw new NullPointerException("ID must not be null");
        }
        return Optional.ofNullable(entities.get(id));
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
     * @return an {@link Optional} containing the saved entity, or an empty {@code Optional} if the entity already
     *         exists in the system
     * @throws EntityAlreadyExistsException if the entity already exists in the system
     * @throws NullPointerException if the provided entity is null
     */
    public Optional<E> save(E entity) throws EntityAlreadyExistsException {
        if (entity == null) {
            throw new NullPointerException("Entity must not be null");
        }

        validator.validate(entity);

        boolean exists = entities.values().stream().anyMatch(existingEntity -> existingEntity.equals(entity));
        if (exists) {
            throw new EntityAlreadyExistsException();
        }

        entities.put(entity.getId(), entity);
        return Optional.of(entity);
    }

    /**
     * Deletes an entity from the repository (storage) by its identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @return an {@link Optional} containing the deleted entity, or an empty {@code Optional} if no entity with the
     *         specified ID exists
     * @throws NullPointerException if the provided identifier is null
     */
    @Override
    public Optional<E> delete(ID id) {
        if (id == null) {
            throw new NullPointerException("ID must not be null");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    /**
     * Updates an existing entity in the repository (storage).
     *
     * @param entity the entity to be updated
     * @return an {@link Optional} containing the updated entity
     * @throws EntityNotFoundException if the entity does not exist in the system
     * @throws NullPointerException if the provided entity is null
     */
    @Override
    public Optional<E> update(E entity) throws EntityNotFoundException {
        if (entity == null) {
            throw new NullPointerException("Entity must not be null");
        }

        if (!entities.containsKey(entity.getId())) {
            throw new EntityNotFoundException("Entity does not exist and cannot be updated.");
        }

        validator.validate(entity);
        entities.put(entity.getId(), entity);
        return Optional.of(entity);
    }
}
