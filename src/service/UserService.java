package service;

import models.Friendship;
import models.User;
import repository.Repository;

import java.util.Optional;

/**
 * Specialized service class for performing operations on users via repository.
 */
public class UserService extends AbstractService<Long, User> {

    private final Service<Long, Friendship> friendshipService;

    /**
     * Constructs a new UserService with the specified repository.
     * This class uses {@link service.FriendshipService} to manage relationships if users support modifications.
     *
     * @param repository the repository used to perform operations on persisting data
     * @param friendshipService the repository used to manage relationships between users
     */
    public UserService(Repository<Long, User> repository, Service<Long, Friendship> friendshipService) {
        super(repository);
        this.friendshipService = friendshipService;
    }


    /**
     * Deletes a user from the repository (storage) by its identifier and updates the specified file.
     * The friendship relationships are updated if users support any modification.
     *
     * @param id the unique identifier of the user to be deleted
     * @return an {@link Optional} containing the deleted user, or an empty {@code Optional} if no user with the
     *         specified ID exists
     */
    @Override
    public Optional<User> deleteById(Long id) {
        Iterable<Friendship> friendships = friendshipService.findAll();
        for (Friendship friendship : friendships) {
            if (friendship.containsUser(id)) {
                friendshipService.deleteById(friendship.getId());
            }
        }

        return super.deleteById(id);
    }
}
