import util.Currency;
import util.CurrencyBasket;
import util.Money;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MovementList {
    private static class ReportItem implements MovementReportItem {
        final String serviceName;
        final CurrencyBasket currencyBasket;

        ReportItem(String serviceName) {
            this.serviceName = serviceName;
            this.currencyBasket = new CurrencyBasket();
        }

        @Override
        public String getServiceName() {
            return serviceName;
        }

        @Override
        public Money getMoney(Currency currency) {
            return currencyBasket.get(currency);
        }

        void addMoney(Money money, Currency currency) {
            currencyBasket.add(money, currency);
        }
    }

    private static class Report implements MovementReport {
        final MovementType type;
        final Map<String, ReportItem> items;
        final CurrencyBasket currencyBasket;

        Report(MovementType type) {
            this.type = type;
            this.items = new TreeMap<>();
            this.currencyBasket = new CurrencyBasket();
        }

        @Override
        public MovementType getType() {
            return type;
        }

        @Override
        public Collection<MovementReportItem> getItems() {
            return Collections.unmodifiableCollection(items.values());
        }

        @Override
        public Money getMoney(Currency currency) {
            return currencyBasket.get(currency);
        }

        void addMoney(String serviceName, Money money, Currency currency) {
            ReportItem item = items.computeIfAbsent(serviceName, ReportItem::new);
            item.addMoney(money, currency);
            currencyBasket.add(money, currency);
        }
    }

    private final Map<MovementType, Report> reports;

    public MovementList() {
        reports = new IdentityHashMap<>();
    }

    public MovementReport getReport(MovementType type) {
        return reports.get(type);
    }

    public void addItem(MovementListItem item) {
        Report report = reports.computeIfAbsent(item.getType(), Report::new);
        report.addMoney(item.getServiceName(), item.getMoney(), item.getCurrency());
    }
}
