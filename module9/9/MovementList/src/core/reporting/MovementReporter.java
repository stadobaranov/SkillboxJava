package core.reporting;

import core.Currency;
import core.CurrencyMap;
import core.Money;
import core.MovementRecord;
import core.MovementType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MovementReporter {
    public MovementReport report(List<MovementRecord> records) {
        Map<MovementType, Map<String, List<MovementRecord>>> groupedRecords = groupRecords(records);

        CurrencyAccumulator totalIncome = new CurrencyAccumulator();
        CurrencyAccumulator totalExpense = new CurrencyAccumulator();

        List<MovementReportRecord> incomeRecords = buildReportRecords(
            groupedRecords, MovementType.INCOME, totalIncome
        );

        List<MovementReportRecord> expenseRecords = buildReportRecords(
            groupedRecords, MovementType.EXPENSE, totalExpense
        );

        return new MovementReport(incomeRecords, expenseRecords, totalIncome, totalExpense);
    }

    private static Map<MovementType, Map<String, List<MovementRecord>>> groupRecords(List<MovementRecord> records) {
        Map<MovementType, Map<String, List<MovementRecord>>> groupedRecords = new IdentityHashMap<>();

        for(MovementRecord record: records) {
            Map<String, List<MovementRecord>> typeRecords = groupedRecords.computeIfAbsent(
                record.getType(), (type) -> new TreeMap<>()
            );

            List<MovementRecord> typedServiceRecords = typeRecords.computeIfAbsent(
                record.getServiceName(), (serviceName) -> new ArrayList<>()
            );

            typedServiceRecords.add(record);
        }

        return groupedRecords;
    }

    private static List<MovementReportRecord> buildReportRecords(
            Map<MovementType, Map<String, List<MovementRecord>>> groupedRecords,
            MovementType type,
            CurrencyAccumulator totalSums) {
        Map<String, List<MovementRecord>> typeRecords = groupedRecords.get(type);

        if(typeRecords == null) {
            return Collections.emptyList();
        }

        List<MovementReportRecord> reportRecords = new ArrayList<>();

        for(Map.Entry<String, List<MovementRecord>> entry: typeRecords.entrySet()) {
            CurrencyAccumulator sums = new CurrencyAccumulator();

            for(MovementRecord record: entry.getValue()) {
                sums.add(record.getSum(), record.getCurrency());
                totalSums.add(record.getSum(), record.getCurrency());
            }

            reportRecords.add(new MovementReportRecord(entry.getKey(), sums));
        }

        return reportRecords;
    }

    private static class CurrencyAccumulator implements CurrencyMap {
        final Map<Currency, Money> map;

        CurrencyAccumulator() {
            map = new IdentityHashMap<>();
        }

        @Override
        public Money get(Currency currency) {
            return map.get(currency);
        }

        void add(Money money, Currency currency) {
            Money current = map.get(currency);

            if(current == null) {
                current = Money.of(0);
            }

            map.put(currency, current.plus(money));
        }
    }
}
