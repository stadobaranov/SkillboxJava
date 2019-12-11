import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class QueuedCarNumberConsumer extends Thread {
    private final String directoryPath;
    private final BlockingQueue<String> queue;
    private volatile boolean shutdown;

    public QueuedCarNumberConsumer(String directoryPath, BlockingQueue<String> queue) {
        this.directoryPath = directoryPath;
        this.queue = queue;
    }

    @Override
    public void run() {
        String fileName = directoryPath + File.separator + "numbers";

        try(FileWriter writer = new FileWriter(fileName)) {
            while(!shutdown) {
                try {
                    writer.write(queue.take());
                }
                catch(InterruptedException exception) {}
            }

            String numbers;

            for(;;) {
                numbers = queue.poll();

                if(numbers == null) {
                    return;
                }

                writer.write(numbers);
            }
        }
        catch(IOException exception) {
            System.out.println("Во время записи автомобильных госномеров возникло исключение:");
            exception.getCause().printStackTrace(System.out);
        }
    }

    public void shutdown() {
        shutdown = true;
    }
}
