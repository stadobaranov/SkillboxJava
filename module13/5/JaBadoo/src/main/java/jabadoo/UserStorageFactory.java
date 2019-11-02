package jabadoo;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.redisson.config.Config;

public class UserStorageFactory implements AutoCloseable {
    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int DEFAULT_PORT = 6379;

    private final String host;
    private final int port;
    private RedissonClient client;

    public UserStorageFactory(String host) {
        this(host, DEFAULT_PORT);
    }

    public UserStorageFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public UserStorage create() {
        if(client == null) {
            Config config = new Config();

            config.useSingleServer()
                  .setTimeout(DEFAULT_TIMEOUT)
                  .setAddress(String.format("redis://%s:%d", host, port));

            try {
                client = Redisson.create(config);
            }
            catch(RedisException exception) {
                throw new UserStorageException("Не удалось подключиться к серверу redis", exception);
            }

            client.getKeys().delete(UserStorageKeys.NAMES);
        }

        return new UserStorage(client);
    }

    @Override
    public void close() {
        if(client != null) {
            client.shutdown();
        }
    }
}
