package models;

import utils.PasswordUtils;

import java.util.Objects;

/**
 * Represents a User entity in the system network with a unique ID, username, hashed password and email.
 * The {@code User} extends the {@code Entity} base class, using {@code Long} as the type of its identifier.
 */
public class User extends Entity<Long> {

    private String username;
    private String password;
    private String email;

    /**
     * Constructs a new {@code User} with the specified username, password (hashed), and email.
     * The password is hashed with the SHA-256 algorithm using {@code PasswordUtils}.
     *
     * @param username the username of the user
     * @param password the plain text password of the user (alternatively hashed)
     * @param email    the email of the user
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = PasswordUtils.hashPassword(password);
        this.email = email;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the hashed password of the user.
     *
     * @return the hashed password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user and hashes it using {@code PasswordUtils}.
     *
     * @param password the new plain text password of the user (alternatively hashed)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the new email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a string representation of the user.
     * The string contains the ID, class name, username, password (hashed), and the email address of the user.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User { " +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                " }";
    }

    /**
     * Compares this user with another object for equality.
     * Two users are considered equal if their IDs, usernames, or email addresses are equal.
     *
     * @param o the object to be compared
     * @return {@code true} if this user is equal to the object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.getId()) &&
                Objects.equals(username, user.getUsername()) &&
                Objects.equals(email, user.getEmail());
    }

    /**
     * Returns the hash code of this user, based on its identifier, username, and email.
     *
     * @return the hash code value of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}
