public enum SizeUnit {
    B(0) {
        @Override
        public String toString(long bytes) {
            return String.format("%d %s", bytes, this);
        }
    },

    KB(1024),
    MB(1024 * 1024),
    GB(1024 * 1024 * 1024);

    private final long threshold;

    SizeUnit(long threshold) {
        this.threshold = threshold;
    }

    public String toString(long bytes) {
        return String.format("%.2f %s", bytes / (double)threshold, this);
    }

    public static String format(long bytes) {
        SizeUnit unit = null;

        for(SizeUnit u: values()) {
            if(bytes >= u.threshold && (unit == null || unit.threshold < u.threshold)) {
                unit = u;
            }
        }

        return unit.toString(bytes);
    }
}
