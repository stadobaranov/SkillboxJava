import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.Callable;

public class ParallelCarNumberGenerationTask implements Callable<Void> {
    private static final int BUFFER_SIZE = 8 * 1024;

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

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName), BUFFER_SIZE)) {
            for(int i = startRegion; i < endRegion; i++) {
                for(int j = 0; j < letters.length; j++) {
                    for(int k = 0; k < letters.length; k++) {
                        for(int l = 0; l < letters.length; l++) {
                            for(int m = 1; m < 1000; m++) {
                                writer.write(letters[j]);
                                writePaddedNumberTo(writer, m, 3);
                                writer.write(letters[k]);
                                writer.write(letters[l]);
                                writePaddedNumberTo(writer, i, 2);
                                writer.newLine();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private static void writePaddedNumberTo(Writer writer, int number, int length) throws IOException {
        String numberString = String.valueOf(number);

        for(int i = 0, e = length - numberString.length(); i < e; i++) {
            writer.write("0");
        }

        writer.write(numberString);
    }
}
