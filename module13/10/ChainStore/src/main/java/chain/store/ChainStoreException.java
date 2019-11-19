package chain.store;

public class ChainStoreException extends RuntimeException {
    public ChainStoreException(String message) {
        super(message);
    }

    public ChainStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
