import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ParallelCarNumberGenerationTask implements Callable<Void> {
    private final String directoryPath;
    private final int startRegion;
    private final int endRegion;

    public ParallelCarNumberGenerationTask(String directoryPath, int startRegion, int endRegion) {
        this.directoryPath = directoryPath;
        this.startRegion = startRegion;
        this.endRegion = endRegion;
    }

    @Override
    public Void call() throws IOException {
        char letters[] = CarNumberGeneratorUtils.getLetters();
        String fileName = directoryPath + File.separator + startRegion + '-' + (endRegion - 1);

        try(FileWriter writer = new FileWriter(fileName)) {
            StringBuilder builder = new StringBuilder();

            for(int i = startRegion; i < endRegion; i++) {
                for(int j = 0; j < letters.length; j++) {
                    for(int k = 0; k < letters.length; k++) {
                        for(int l = 0; l < letters.length; l++) {
                            for(int m = 1; m < 1000; m++) {
                                builder.append(letters[j]);
                                CarNumberGeneratorUtils.appendPaddedNumberTo(builder, m, 3);
                                builder.append(letters[k]);
                                builder.append(letters[l]);
                                CarNumberGeneratorUtils.appendPaddedNumberTo(builder, i, 2);
                                builder.append('\n');
                            }

                            writer.write(builder.toString());
                            builder.setLength(0);
                        }
                    }
                }
            }
        }

        return null;
    }
}
