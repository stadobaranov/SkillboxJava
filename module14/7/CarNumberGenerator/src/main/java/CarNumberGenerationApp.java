import java.util.concurrent.TimeUnit;

public class CarNumberGenerationApp {
    public static void main(String... args) {
        CarNumberGenerationMethod methods[] = {
            new ParallelCarNumberGenerator(
                "src/main/resources/1",
                1,
                createListener("ParallelCarNumberGenerator(tasks = 1)")
            ),

            new ParallelCarNumberGenerator(
                "src/main/resources/2",
                Runtime.getRuntime().availableProcessors(),
                createListener("ParallelCarNumberGenerator(tasks = cores)")
            ),

            new ParallelCarNumberGenerator(
                "src/main/resources/3",
                CarNumberGeneratorUtils.getRegionCount(),
                createListener("ParallelCarNumberGenerator(tasks = regions)")
            ),

            new QueuedCarNumberGenerator(
                "src/main/resources/4",
                1,
                1000,
                createListener("QueuedCarNumberGenerator(producers = 1, queueCapacity = 1000)")
            ),

            new QueuedCarNumberGenerator(
                "src/main/resources/5",
                Math.max(1, Runtime.getRuntime().availableProcessors() - 1),
                1000,
                createListener("QueuedCarNumberGenerator(producers = cores - 1, queueCapacity = 1000)")
            ),

            new QueuedCarNumberGenerator(
                "src/main/resources/6",
                1,
                100000,
                createListener("QueuedCarNumberGenerator(producers = 1, queueCapacity = 100000)")
            ),

            new QueuedCarNumberGenerator(
                "src/main/resources/7",
                Math.max(1, Runtime.getRuntime().availableProcessors() - 1),
                100000,
                createListener("QueuedCarNumberGenerator(producers = cores - 1, queueCapacity = 100000)")
            ),

            new QueuedCarNumberGenerator(
                "src/main/resources/8",
                1,
                Integer.MAX_VALUE,
                createListener("QueuedCarNumberGenerator(producers = 1, queueCapacity = max)")
            ),

            new QueuedCarNumberGenerator(
                "src/main/resources/9",
                Math.max(1, Runtime.getRuntime().availableProcessors() - 1),
                Integer.MAX_VALUE,
                createListener("QueuedCarNumberGenerator(producers = cores - 1, queueCapacity = max)")
            ),
        };

        for(CarNumberGenerationMethod method: methods) {
            method.generate();
        }
    }

    private static CarNumberGenerationListener createListener(String generatorId) {
        return new CarNumberGenerationListener() {
            private long timestamp;

            @Override
            public void beforeGeneration() {
                timestamp = System.nanoTime();
            }

            @Override
            public void afterGeneration() {
                System.out.printf(
                    "%s: %d мс%n", generatorId, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timestamp)
                );
            }
        };
    }
}
