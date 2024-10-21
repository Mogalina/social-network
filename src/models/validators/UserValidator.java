package models.validators;

import exceptions.ValidationException;
import models.User;
import utils.EmailUtils;
import utils.UsernameUtils;

/**
 * Validator responsible for validating {@link User} entities.
 * This class implement the Validator interface for the User type.
 */
public class UserValidator implements Validator<User> {

    /**
     * Validates the provided {@link User} entity.
     *
     * @param user the entity to be validated
     * @throws ValidationException if the user is null or fails validation criteria
     */
    @Override
    public void validate(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("User must not be null");
        }

        UsernameUtils.checkNameFormat(user.getFirstName()); // Validate first name format
        UsernameUtils.checkNameFormat(user.getLastName()); // Validate last name format
        EmailUtils.checkEmailFormat(user.getEmail()); // Validate email address format
    }
}
