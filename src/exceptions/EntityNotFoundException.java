package exceptions;

/**
 * Exception to indicate that an entity doesn't exist in the system.
 */
public class EntityNotFoundException extends Throwable {

    /**
     * Constructs a new EntityNotFoundException with a default message.
     */
    public EntityNotFoundException() {
        super("Entity not found");
    }

    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     *
     * @param message the detail message of the exception
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
