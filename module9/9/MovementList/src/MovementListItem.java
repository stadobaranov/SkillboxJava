import util.Currency;
import util.Money;

public class MovementListItem {
    private final String serviceName;
    private final MovementType type;
    private final Money money;
    private final Currency currency;

    public MovementListItem(String serviceName, MovementType type, Money money, Currency currency) {
        this.serviceName = serviceName;
        this.type = type;
        this.money = money;
        this.currency = currency;
    }

    public String getServiceName() {
        return serviceName;
    }

    public MovementType getType() {
        return type;
    }

    public Money getMoney() {
        return money;
    }

    public Currency getCurrency() {
        return currency;
    }
}
