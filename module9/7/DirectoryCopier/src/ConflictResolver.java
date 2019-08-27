import java.io.File;
import java.util.Scanner;

public class ConflictResolver {
    private final Scanner scanner;

    public ConflictResolver(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean resolveDirectoryMaking(File directory) {
        return prompt(
            String.format("Папка \"%s\" не существует, создать? [Д/Н]: ", directory.getAbsolutePath()),
                Answer.YES) == Answer.YES;
    }

    public boolean resolveFileToDirectoryOverwriting(File file) {
        return prompt(
            String.format("Заменить существующий файл \"%s\" на папку? [Д/Н]: ", file.getAbsolutePath()),
                Answer.NO) == Answer.YES;
    }

    public boolean resolveDirectoryToFileOverwriting(File file) {
        return prompt(
            String.format("Заменить существующую папку \"%s\" на файл? [Д/Н]: ", file.getAbsolutePath()),
                Answer.NO) == Answer.YES;
    }

    public boolean resolveFileOverwriting(File file) {
        return prompt(
            String.format("Заменить существующий файл \"%s\"? [Д/Н]: ", file.getAbsolutePath()),
                Answer.NO) == Answer.YES;
    }

    private Answer prompt(String question, Answer defaultAnswer) {
        for(;;) {
            System.out.print(question);
            String line = scanner.nextLine();

            if(defaultAnswer != null && line.isEmpty()) {
                return defaultAnswer;
            }

            for(Answer answer: Answer.values()) {
                if(answer.match(line)) {
                    return answer;
                }
            }
        }
    }

    private static enum Answer {
        YES("y", "yes", "д", "да"),
        NO("n", "no", "н", "нет");

        private final String values[];

        Answer(String... values) {
            this.values = values;
        }

        public boolean match(String s) {
            for(String value: values) {
                if(value.equalsIgnoreCase(s)) {
                    return true;
                }
            }

            return false;
        }
    }
}
