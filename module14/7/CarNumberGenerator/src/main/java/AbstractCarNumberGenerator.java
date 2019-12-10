public abstract class AbstractCarNumberGenerator implements CarNumberGenerationMethod {
    private final CarNumberGenerationListener listener;

    public AbstractCarNumberGenerator() {
        this(null);
    }

    public AbstractCarNumberGenerator(CarNumberGenerationListener listener) {
        this.listener = listener;
    }

    @Override
    public void generate() {
        initialize();

        try {
            if(listener != null) {
                listener.beforeGeneration();
            }

            runGeneration();

            if(listener != null) {
                listener.afterGeneration();
            }
        }
        finally {
            release();
        }
    }

    protected void initialize() {}

    protected abstract void runGeneration();

    protected void release() {}
}
