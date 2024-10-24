import models.Friendship;
import models.User;
import models.validators.FriendshipValidator;
import models.validators.UserValidator;
import models.validators.Validator;
import repository.FriendshipFileRepository;
import repository.Repository;
import repository.UserFileRepository;
import service.FriendshipService;
import service.Network;
import ui.SocialNetworkApplication;
import service.UserService;
import service.Service;
import utils.Config;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Validator<User> userValidator = new UserValidator();
        Repository<String, User> userRepository = new UserFileRepository(Config.DEFAULT_LOCAL_USER_STORAGE, userValidator);
        Service<String, User> userService = new UserService(userRepository);

        Validator<Friendship> friendshipValidator = new FriendshipValidator(userRepository);
        Repository<String, Friendship> friendshipRepository = new FriendshipFileRepository(Config.DEFAULT_LOCAL_FRIENDSHIP_STORAGE, friendshipValidator);
        Service<String, Friendship> friendshipService = new FriendshipService(friendshipRepository);

        Network network = new Network(userService, friendshipService);

        SocialNetworkApplication socialNetwork = new SocialNetworkApplication(network);
        socialNetwork.runApplication();
    }
}