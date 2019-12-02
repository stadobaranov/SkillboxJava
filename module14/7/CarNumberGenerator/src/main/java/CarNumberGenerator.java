import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CarNumberGenerator {
    private static final String DIRECTORY_PATH = "src/main/resources";

    private static final char LETTERS[] = {
        'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'
    };

    private static final int REGION_COUNT = 99;

    public static void main(String... args) {
        long startTime = System.nanoTime();

        // int taskCount = 1;
        // int taskCount = Math.min(REGION_COUNT, Runtime.getRuntime().availableProcessors());
        int taskCount = REGION_COUNT;

        ExecutorService taskExecutor = Executors.newFixedThreadPool(taskCount);
        Future<?> taskFutures[] = new Future<?>[taskCount];

        int regionsPerTask = (int)Math.ceil(REGION_COUNT / (double)taskCount);

        for(int i = 0; i < taskCount; i++) {
            int startRegion = i * regionsPerTask + 1;
            int endRegion = Math.min(startRegion + regionsPerTask, REGION_COUNT + 1);

            taskFutures[i] = taskExecutor.submit(
                new CarNumberGenerationTask(DIRECTORY_PATH, LETTERS, startRegion, endRegion)
            );
        }

        for(Future<?> taskFuture: taskFutures) {
            for(;;) {
                try {
                    taskFuture.get();
                }
                catch(InterruptedException exception) {
                    continue;
                }
                catch(ExecutionException exception) {
                    System.out.println("Во время генерации автомобильного госномера возникло исключение:");
                    exception.getCause().printStackTrace(System.out);
                }

                break;
            }
        }

        taskExecutor.shutdown();
        // taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        System.out.printf(
            "Время генерации автомобильных госномеров: %d мс%n",
            TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
        );
    }
}
