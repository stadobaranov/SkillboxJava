package core;

public class MovementRecord {
    private final String serviceName;
    private final MovementType type;
    private final Money sum;
    private final Currency currency;

    public MovementRecord(String serviceName, MovementType type, Money sum, Currency currency) {
        this.serviceName = serviceName;
        this.type = type;
        this.sum = sum;
        this.currency = currency;
    }

    public String getServiceName() {
        return serviceName;
    }

    public MovementType getType() {
        return type;
    }

    public Money getSum() {
        return sum;
    }

    public Currency getCurrency() {
        return currency;
    }
}
