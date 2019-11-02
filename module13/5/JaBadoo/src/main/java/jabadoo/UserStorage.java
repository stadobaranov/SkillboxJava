package jabadoo;

import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;

public class UserStorage {
    private final RScoredSortedSet<String> names;

    public UserStorage(RedissonClient client) {
        names = client.getScoredSortedSet(UserStorageKeys.NAMES);
    }

    public String getNextName() {
        try {
            String nextName = names.first();
            names.add(System.currentTimeMillis(), nextName);
            return nextName;
        }
        catch(RedisException exception) {
            throw new UserStorageException("Не удалось получить следующего пользователя", exception);
        }
    }

    public void register(String name) {
        try {
            names.add(System.currentTimeMillis(), name);
        }
        catch(RedisException exception) {
            throw new UserStorageException("Не удалось зарегистрировать пользователя", exception);
        }
    }

    public void top(String name) {
        try {
            names.add(Long.MIN_VALUE, name);
        }
        catch(RedisException exception) {
            throw new UserStorageException("Не удалось поднять в топ пользователя", exception);
        }
    }
}
