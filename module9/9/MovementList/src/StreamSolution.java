import core.Currency;
import core.Money;
import core.MovementRecord;
import core.MovementType;
import parsing.MovementListParser;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StreamSolution {
    public static void run() {
        new MovementListParser()
                .parsedRecords("resources/movementList.csv")
                .map(GroupRecord::of)
                .collect(
                    Collectors.groupingBy(
                        GroupRecord::getIndex,

                        () -> new TreeMap<>(Comparator.comparing(a -> a.serviceName)),

                        Collectors.collectingAndThen(
                            Collectors.reducing(null, GroupRecord::combine),

                            (record) -> {
                                System.out.println(record);
                                return null;
                            }
                        )
                    )
                );
    }

    private static class GroupIndex {
        final String serviceName;
        final Currency currency;

        GroupIndex(String serviceName, Currency currency) {
            this.serviceName = serviceName;
            this.currency = currency;
        }
    }

    private static class GroupRecord {
        final GroupIndex index;
        final Money income;
        final Money expense;

        GroupRecord(String serviceName, Money income, Money expense, Currency currency) {
            this(new GroupIndex(serviceName, currency), income, expense);
        }

        GroupRecord(GroupIndex index, Money income, Money expense) {
            this.index = index;
            this.income = income;
            this.expense = expense;
        }

        GroupIndex getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return String.format("%-31s %12s %12s %3s", index.serviceName, income, expense, index.currency);
        }

        static GroupRecord of(MovementRecord record) {
            return record.getType() == MovementType.INCOME?
                ofIncome(record.getServiceName(), record.getSum(), record.getCurrency()):
                ofExpense(record.getServiceName(), record.getSum(), record.getCurrency());
        }

        static GroupRecord ofIncome(String serviceName, Money income, Currency currency) {
            return new GroupRecord(serviceName, income, Money.of(0), currency);
        }

        static GroupRecord ofExpense(String serviceName, Money expense, Currency currency) {
            return new GroupRecord(serviceName, Money.of(0), expense, currency);
        }

        static GroupRecord combine(GroupRecord a, GroupRecord b) {
            if(a == null) {
                return b;
            }

            return new GroupRecord(a.index, a.income.plus(b.income), a.expense.plus(b.expense));
        }
    }
}
