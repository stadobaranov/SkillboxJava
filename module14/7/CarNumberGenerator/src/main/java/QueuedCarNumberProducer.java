import java.util.concurrent.BlockingQueue;

public class QueuedCarNumberProducer extends Thread {
    private final BlockingQueue<? super String> queue;
    private final int startRegion;
    private final int endRegion;

    public QueuedCarNumberProducer(BlockingQueue<? super String> queue, int startRegion, int endRegion) {
        this.queue = queue;
        this.startRegion = startRegion;
        this.endRegion = endRegion;
    }

    @Override
    public void run() {
        char letters[] = CarNumberGeneratorUtils.getLetters();
        StringBuilder builder = new StringBuilder();

        for(int i = startRegion; i < endRegion; i++) {
            for(int j = 0; j < letters.length; j++) {
                for(int k = 0; k < letters.length; k++) {
                    for(int l = 0; l < letters.length; l++) {
                        for(int m = 1; m < 1000; m++) {
                            builder.append(letters[j]);
                            appendPaddedNumberTo(builder, m, 3);
                            builder.append(letters[k]);
                            builder.append(letters[l]);
                            appendPaddedNumberTo(builder, i, 2);
                            builder.append('\n');
                        }

                        for(;;) {
                            try {
                                queue.put(builder.toString());
                            }
                            catch(InterruptedException exception) {
                                continue;
                            }

                            break;
                        }

                        builder.setLength(0);
                    }
                }
            }
        }
    }

    private static void appendPaddedNumberTo(StringBuilder builder, int number, int length) {
        String numberString = String.valueOf(number);

        for(int i = 0, e = length - numberString.length(); i < e; i++) {
            builder.append('0');
        }

        builder.append(numberString);
    }
}
