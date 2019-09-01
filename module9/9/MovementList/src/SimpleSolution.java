import core.reporting.MovementReporter;
import parsing.MovementListParser;
import printing.MovementReportPrinter;

public class SimpleSolution {
    public static void run() {
        MovementListParser listParser = new MovementListParser();
        MovementReporter reporter = new MovementReporter();
        MovementReportPrinter reportPrinter = new MovementReportPrinter();
        reportPrinter.print(reporter.report(listParser.parse("resources/movementList.csv")));
    }
}
