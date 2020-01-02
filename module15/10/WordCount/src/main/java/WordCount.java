import org.apache.spark.sql.SparkSession;
import scala.Tuple2;
import java.util.Arrays;
import java.util.regex.Pattern;

public class WordCount {
    private static final Pattern EXCLUDES = Pattern.compile("[.,;:?!\\-—\"«»()\\[\\]…\t\n]");
    private static final Pattern SPACE = Pattern.compile("\\s+");

    public static void main(String... args) {
        if(args.length != 2) {
            System.err.println("Usage: WordCount <inFile> <outFile>");
            return;
        }

        SparkSession spark = SparkSession.builder()
                                         .appName("WordCount")
                                         .config("spark.master", "local")
                                         .getOrCreate();

        spark.read()
             .textFile(args[0])
             .javaRDD()
             .map(line -> EXCLUDES.matcher(line).replaceAll(" ").toUpperCase())
             .flatMap(line -> Arrays.asList(SPACE.split(line)).iterator())
             .filter(word -> !word.isEmpty())
             .mapToPair(word -> new Tuple2<>(word, 1))
             .reduceByKey(Integer::sum)
             .mapToPair(Tuple2::swap)
             .sortByKey(false, 1)
             .mapToPair(Tuple2::swap)
             .saveAsTextFile(args[1]);
    }
}
