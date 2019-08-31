import util.Currency;
import util.Money;

public interface MovementReportItem {
    public abstract String getServiceName();
    public abstract Money getMoney(Currency currency);
}
