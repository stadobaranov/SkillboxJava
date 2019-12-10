import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelCarNumberGenerator extends AbstractCarNumberGenerator {
    private final String directoryPath;
    private final int taskCount;
    private ExecutorService taskExecutor;

    public ParallelCarNumberGenerator(String directoryPath, int taskCount) {
        this(directoryPath, taskCount, null);
    }

    public ParallelCarNumberGenerator(String directoryPath, int taskCount, CarNumberGenerationListener listener) {
        super(listener);
        this.directoryPath = directoryPath;
        this.taskCount = limitTaskCount(taskCount);
    }

    private static int limitTaskCount(int taskCount) {
        return Math.min(taskCount, CarNumberGeneratorUtils.getRegionCount());
    }

    @Override
    protected void initialize() {
        CarNumberGeneratorUtils.makeDestinationDir(directoryPath);
        this.taskExecutor = Executors.newFixedThreadPool(taskCount);
    }

    @Override
    protected void runGeneration() {
        Future<?> taskFutures[] = new Future<?>[taskCount];
        int regionCount = CarNumberGeneratorUtils.getRegionCount();
        int regionsPerTask = (int)Math.ceil(regionCount / (double)taskCount);

        for(int i = 0; i < taskCount; i++) {
            int startRegion = i * regionsPerTask + 1;
            int endRegion = Math.min(startRegion + regionsPerTask, regionCount + 1);

            taskFutures[i] = taskExecutor.submit(
                new ParallelCarNumberGenerationTask(directoryPath, startRegion, endRegion)
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
    }

    @Override
    protected void release() {
        taskExecutor.shutdown();

        for(;;) {
            try {
                taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            }
            catch(InterruptedException exception) {
                continue;
            }

            break;
        }
    }
}
