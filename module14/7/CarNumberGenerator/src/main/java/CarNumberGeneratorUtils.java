import java.io.File;

public class CarNumberGeneratorUtils {
    private static final char LETTERS[] = {
        'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'
    };

    private static final int REGION_COUNT = 99;

    public static char[] getLetters() {
        return LETTERS;
    }

    public static int getRegionCount() {
        return REGION_COUNT;
    }

    public static void makeDestinationDir(String directoryPath) {
        File directory = new File(directoryPath);

        if(!directory.exists()) {
            if(!directory.mkdirs()) {
                throw new CarNumberGenerationException(
                        String.format("Не удалось создать директорию \"%s\"", directoryPath)
                );
            }
        }
        else {
            File files[] = directory.listFiles();

            if(files != null) {
                for(File file: files) {
                    if(file.isFile())
                        file.delete();
                }
            }
        }
    }

    public static void appendPaddedNumberTo(StringBuilder builder, int number, int length) {
        String numberString = String.valueOf(number);

        for(int i = 0, e = length - numberString.length(); i < e; i++) {
            builder.append('0');
        }

        builder.append(numberString);
    }
}
