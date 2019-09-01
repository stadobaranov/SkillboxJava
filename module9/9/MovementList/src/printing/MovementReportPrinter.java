package printing;

import core.Currency;
import core.CurrencyMap;
import core.Money;
import core.reporting.MovementReport;
import core.reporting.MovementReportRecord;
import java.util.Collection;

public class MovementReportPrinter {
    public void print(MovementReport report) {
        printTable("ПРИХОД", report.getIncomeRecords(), report.getTotalIncome());
        printTable("РАСХОД", report.getExpenseRecords(), report.getTotalExpense());
    }

    private static void printTable(String title, Collection<MovementReportRecord> records, CurrencyMap totalSums) {
        System.out.println(title);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("| Сервис                          |     RUR      |     EUR      |     USD      |");
        System.out.println("--------------------------------------------------------------------------------");

        for(MovementReportRecord record: records) {
            CurrencyMap sums = record.getSums();

            System.out.printf(
                "| %-31s | %12s | %12s | %12s |%n",
                record.getServiceName(),
                moneyToString(sums.get(Currency.RUR)),
                moneyToString(sums.get(Currency.EUR)),
                moneyToString(sums.get(Currency.USD))
            );
        }

        System.out.println("--------------------------------------------------------------------------------");

        System.out.printf(
            "|                                 | %12s | %12s | %12s |%n",
            moneyToString(totalSums.get(Currency.RUR)),
            moneyToString(totalSums.get(Currency.EUR)),
            moneyToString(totalSums.get(Currency.USD))
        );

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
    }

    private static String moneyToString(Money money) {
        return money != null? money.toString(): "";
    }
}
