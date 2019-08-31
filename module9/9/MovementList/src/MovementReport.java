import util.Currency;
import util.Money;
import java.util.Collection;

public interface MovementReport {
    public abstract MovementType getType();
    public abstract Collection<MovementReportItem> getItems();
    public abstract Money getMoney(Currency currency);
}
