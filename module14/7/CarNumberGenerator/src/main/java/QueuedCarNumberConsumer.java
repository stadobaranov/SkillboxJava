import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class QueuedCarNumberConsumer extends Thread {
    private static final int BUFFER_SIZE = 8 * 1024;

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

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName), BUFFER_SIZE)) {
            while(!shutdown) {
                try {
                    writeTo(writer, queue.take());
                }
                catch(InterruptedException exception) {}
            }

            String number;

            for(;;) {
                number = queue.poll();

                if(number == null) {
                    return;
                }

                writeTo(writer, number);
            }
        }
        catch(IOException exception) {
            System.out.println("Во время записи автомобильных госномеров возникло исключение:");
            exception.getCause().printStackTrace(System.out);
        }
    }

    private static void writeTo(BufferedWriter writer, String number) throws IOException {
        writer.write(number);
        writer.newLine();
    }

    public void shutdown() {
        shutdown = true;
    }
}
