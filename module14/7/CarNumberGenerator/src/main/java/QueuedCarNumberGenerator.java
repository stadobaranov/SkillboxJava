import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueuedCarNumberGenerator extends AbstractCarNumberGenerator {
    private final String directoryPath;
    private final int producerCount;
    private final int queueCapacity;

    public QueuedCarNumberGenerator(String directoryPath, int producerCount, int queueCapacity) {
        this(directoryPath, producerCount, queueCapacity, null);
    }

    public QueuedCarNumberGenerator(String directoryPath, int producerCount, int queueCapacity,
            CarNumberGenerationListener listener) {
        super(listener);
        this.directoryPath = directoryPath;
        this.producerCount = producerCount;
        this.queueCapacity = queueCapacity;
    }

    @Override
    protected void initialize() {
        CarNumberGeneratorUtils.makeDestinationDir(directoryPath);
    }

    @Override
    protected void runGeneration() {
        int regionCount = CarNumberGeneratorUtils.getRegionCount();
        int regionsPerTask = (int)Math.ceil(regionCount / (double)producerCount);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(queueCapacity);

        QueuedCarNumberProducer producers[] = new QueuedCarNumberProducer[producerCount];

        for(int i = 0; i < producerCount; i++) {
            int startRegion = i * regionsPerTask + 1;
            int endRegion = Math.min(startRegion + regionsPerTask, regionCount + 1);
            QueuedCarNumberProducer producer = new QueuedCarNumberProducer(queue, startRegion, endRegion);
            producers[i] = producer;
            producer.start();
        }

        QueuedCarNumberConsumer consumer = new QueuedCarNumberConsumer(directoryPath, queue);
        consumer.start();

        for(QueuedCarNumberProducer producer: producers) {
            join(producer);
        }

        consumer.shutdown();
        consumer.interrupt();

        join(consumer);
    }

    private static void join(Thread thread) {
        for(;;) {
            try {
                thread.join();
            }
            catch(InterruptedException exception) {
                continue;
            }

            break;
        }
    }
}
