package service;

import models.User;
import repository.Repository;

/**
 * Specialized service class for performing operations on users via repository.
 */
public class UserService extends AbstractService<String, User> {

    /**
     * Constructs a new UserService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public UserService(Repository<String, User> repository) {
        super(repository);
    }
}
