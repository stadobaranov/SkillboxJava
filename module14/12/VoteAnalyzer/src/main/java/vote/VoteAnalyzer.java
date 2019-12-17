package vote;

import vote.parsing.VoteParser;
import vote.parsing.sax.SaxVoteParser;
import java.io.File;
import java.util.function.Supplier;

public class VoteAnalyzer {
    private static final Runtime runtime = Runtime.getRuntime();

    private static long getUsageMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static void main(String... args) {
        parse("SaxVoteParser", SaxVoteParser::new);
        // parse("DomVoteParser", DomVoteParser::new);
    }

    private static void parse(String name, Supplier<VoteParser> factory) {
        long usage = getUsageMemory();
        VoteParser parser = factory.get();
        VoterList list = parser.parse(new File("src/main/resources/data-18M.xml"));
        System.out.printf("Разбор при помощи %s занял: %d байт%n", name, getUsageMemory() - usage);
        // list.printDuplicatedVoters(System.out);
    }
}
