package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Money {
    private static final Money ZERO = new Money(0);

    private static final Pattern pattern = Pattern.compile(
        "(0|(?:[1-9]\\d*))\\.(\\d{2})"
    );

    private final long amount;

    private Money(long amount) {
        this.amount = amount;
    }

    public Money plus(Money other) {
        return of(amount + other.amount);
    }

    public String toString() {
        long whole = amount / 100;

        return String.format(
            "%d.%02d", whole, Math.abs(amount - (whole * 100))
        );
    }

    public static Money of(long amount) {
        return amount == 0? ZERO: new Money(amount);
    }

    public static Money parse(String raw) {
        Matcher matcher = pattern.matcher(raw);

        if(!matcher.matches()) {
            throw new MoneyFormatException(
                String.format("Недопустимый формат денежных средств - \"%s\"", raw));
        }

        return of(Long.parseLong(matcher.group(1)) * 100 + Long.parseLong(matcher.group(2)));
    }
}
