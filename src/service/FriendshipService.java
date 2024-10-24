package service;

import models.Friendship;
import models.User;
import repository.Repository;

import java.util.Optional;

/**
 * Specialized service class for performing operations on friendship relationships between users via repository.
 */
public class FriendshipService extends AbstractService<String, Friendship> {

    /**
     * Constructs a new FriendshipService with the specified repository.
     *
     * @param repository the repository used to perform operations on persisting data
     */
    public FriendshipService(Repository<String, Friendship> repository) {
        super(repository);
    }
}
