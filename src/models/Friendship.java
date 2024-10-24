package models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Friendship entity in the system newtork with a unique ID, sender id, receiver id, request date and a
 * request pending state.
 * The {@code Friendship} extends the {@code Entity} base class, using {@code Long} as the type of its identifier.
 */
public class Friendship extends Entity<Long> {

    private final Long uid1; // Sender's identifier
    private final Long uid2; // Receiver's identifier
    private LocalDateTime date; // The request date
    private boolean pending; // Request pending state

    /**
     * Constructs a new {@code Friendship} with the specified sender id, receiver id and request date.
     *
     * @param uid1 the identifier of the sender user
     * @param uid2 the identifier of the receiver user
     */
    public Friendship(Long uid1, Long uid2) {
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.date = LocalDateTime.now(); // Automatically sets the current date and time
        this.pending = true;
    }

    /**
     * Returns the sender's identifier.
     *
     * @return the identifier of the sender
     */
    public Long getSenderId() {
        return uid1;
    }

    /**
     * Returns the receiver's identifier.
     *
     * @return the identifier of the receiver
     */
    public Long getReceiverId() {
        return uid2;
    }

    /**
     * Returns the request date of the friendship.
     *
     * @return the request date of the friendship.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date of the friendship request.
     *
     * @param date the request date of the friendship.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the pending state of the friendship request.
     *
     * @return {@code true} if the request is still waiting for a response, {@code false} otherwise
     */
    public boolean isPending() {
        return pending;
    }

    /**
     * Sets the pending state of the friendship request.
     *
     * @param pending the new pending state of the friendship request
     */
    public void setPending(boolean pending) {
        this.pending = pending;
    }

    /**
     * Returns the identifiers of the users involved in the friendship request.
     *
     * @return an array containing the sender id and receiver id
     */
    public Long[] getUsers() {
        return new Long[]{uid1, uid2};
    }

    /**
     * Checks if the provided user identifier is involved in the friendship request.
     *
     * @param uid the user identifier to check
     * @return {@code true} if the user is involed in the friendship request, {@code false} otherwise
     */
    public boolean containsUser(Long uid) {
        return uid1.equals(uid) || uid2.equals(uid);
    }

    /**
     * Returns a string representation of the friendship.
     * The string contains the ID, sender id, receiver id, request date and pending state.
     *
     * @return a string representation of the friendship
     */
    @Override
    public String toString() {
        return "Friendship { " +
                "id=" + id + '\'' +
                "uid1=" + uid1 + '\'' +
                "uid2=" + uid2 + '\'' +
                "date=" + date + '\'' +
                "pending=" + pending +
                " }";
    }

    /**
     * Compares this friendship with another object for equality.
     * Two friendship requests are considered equal if their IDs, sender ids, receiver ids, request dates and pending
     * states are equal.
     *
     * @param o the object to be compared
     * @return {@code true} if this friendship is equal to the object, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friendship friendship = (Friendship) o;
        return Objects.equals(id, friendship.id) &&
                Objects.equals(uid1, friendship.uid1) &&
                Objects.equals(uid2, friendship.uid2) &&
                Objects.equals(date, friendship.date) &&
                Objects.equals(pending, friendship.pending);
    }

    /**
     * Returns the hash code of this friendship, based on its identifier, sender id, receiver id, request date and
     * pending state.
     *
     * @return the hash code value of the friendship
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, uid1, uid2, date, pending);
    }
}
