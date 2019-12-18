package vote.database;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseUtils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatDate(LocalDate date) {
        return dateFormatter.format(date);
    }
}
