package core;

public class Money {
    private static final Money ZERO = new Money(0);

    private final long amount;

    private Money(long amount) {
        this.amount = amount;
    }

    public Money plus(Money other) {
        return of(amount + other.amount);
    }

    public String toString() {
        long integerPart = amount / 100;

        return String.format(
            "%d.%02d", integerPart, Math.abs(amount - (integerPart * 100)));
    }

    public static Money of(long amount) {
        return amount == 0? ZERO: new Money(amount);
    }
}
