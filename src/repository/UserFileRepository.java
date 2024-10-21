package repository;

import models.User;
import models.validators.Validator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Long, User> {

    public UserFileRepository(String fileName, Validator<User> validator) throws IOException {
        super(fileName, validator);
    }

    @Override
    protected User extractEntity(@NotNull String record) {
        List<String> fields = Arrays.asList(record.split(","));
        Long id = Long.parseLong(fields.get(0));
        String firstName = fields.get(1);
        String lastName = fields.get(2);
        String password = fields.get(3);
        String email = fields.get(4);

        User user = new User(firstName, lastName, password, email);
        user.setId(id);
        return user;
    }

    @Override
    protected String entityToString(@NotNull User user) {
        return user.getId() +
                "," + user.getFirstName() +
                "," + user.getLastName() +
                "," + user.getPassword() +
                "," + user.getEmail();
    }
}
