package service;

import exceptions.EntityNotFoundException;
import models.Entity;
import repository.Repository;

import java.util.Optional;

public abstract class AbstractService<ID, E extends Entity<ID>> implements Service<ID, E> {

    Repository<ID, E> repository;

    public AbstractService(Repository<ID, E> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<E> findById(ID id) {
        return repository.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<E> save(E entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<E> deleteById(ID id) {
        return repository.delete(id);
    }

    @Override
    public Optional<E> update(E entity) throws EntityNotFoundException {
        return repository.update(entity);
    }
}
