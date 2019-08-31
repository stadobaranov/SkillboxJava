public class MovementListApp {
    public static void main(String... args) {
        MovementList list;

        try {
            MovementListParser parser = new MovementListParser();
            list = parser.parse("resources/movementList.csv");
        }
        catch(MovementListParseException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        MovementReportPrinter printer = new MovementReportPrinter();

        for(MovementType type: MovementType.values()) {
            printer.print(list.getReport(type));
        }
    }
}
