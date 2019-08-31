import util.Currency;
import util.Money;
import java.util.IdentityHashMap;
import java.util.Map;

public class MovementReportPrinter {
    private static final Map<MovementType, String> titles;

    static {
        titles = new IdentityHashMap<>();
        titles.put(MovementType.DEBIT, "ПРИХОД");
        titles.put(MovementType.CREDIT, "РАСХОД");
    }

    private static String getTitle(MovementType type) {
        return titles.getOrDefault(type, type.name());
    }

    public void print(MovementReport report) {
        System.out.println(getTitle(report.getType()));
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("| Сервис                          |     RUR      |     EUR      |     USD      |");
        System.out.println("--------------------------------------------------------------------------------");

        for(MovementReportItem item: report.getItems()) {
            System.out.printf(
                "| %-31s | %12s | %12s | %12s |%n",
                item.getServiceName(),
                moneyToString(item.getMoney(Currency.RUR)),
                moneyToString(item.getMoney(Currency.EUR)),
                moneyToString(item.getMoney(Currency.USD))
            );
        }

        System.out.println("--------------------------------------------------------------------------------");

        System.out.printf(
            "|                                 | %12s | %12s | %12s |%n",
            moneyToString(report.getMoney(Currency.RUR)),
            moneyToString(report.getMoney(Currency.EUR)),
            moneyToString(report.getMoney(Currency.USD))
        );

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
    }

    private String moneyToString(Money money) {
        return money != null? money.toString(): "";
    }
}
