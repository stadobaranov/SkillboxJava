package core.reporting;

import core.CurrencyMap;
import java.util.Collection;
import java.util.Collections;

public class MovementReport {
    private final Collection<MovementReportRecord> incomeRecords;
    private final Collection<MovementReportRecord> expenseRecords;
    private final CurrencyMap totalIncome;
    private final CurrencyMap totalExpense;

    public MovementReport(
            Collection<MovementReportRecord> incomeRecords,
            Collection<MovementReportRecord> expenseRecords,
            CurrencyMap totalIncome,
            CurrencyMap totalExpense) {
        this.incomeRecords = incomeRecords;
        this.expenseRecords = expenseRecords;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public Collection<MovementReportRecord> getIncomeRecords() {
        return Collections.unmodifiableCollection(incomeRecords);
    }

    public Collection<MovementReportRecord> getExpenseRecords() {
        return Collections.unmodifiableCollection(expenseRecords);
    }

    public CurrencyMap getTotalIncome() {
        return totalIncome;
    }

    public CurrencyMap getTotalExpense() {
        return totalExpense;
    }
}
