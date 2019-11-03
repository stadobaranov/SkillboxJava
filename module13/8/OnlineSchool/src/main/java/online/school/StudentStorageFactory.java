package online.school;

import com.mongodb.MongoClient;

public class StudentStorageFactory implements AutoCloseable {
    private final String host;
    private final int port;
    private MongoClient client;

    public StudentStorageFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public StudentStorage create() {
        if(client == null) {
            client = new MongoClient(host, port);
        }

        return new StudentStorage(client);
    }

    @Override
    public void close() {
        if(client != null) {
            client.close();
        }
    }
}
