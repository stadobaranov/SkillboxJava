package util;

import java.util.IdentityHashMap;
import java.util.Map;

public class CurrencyBasket {
    private final Map<Currency, Money> map;

    public CurrencyBasket() {
        map = new IdentityHashMap<>();
    }

    public Money get(Currency currency) {
        return map.get(currency);
    }

    public void add(Money money, Currency currency) {
        Money current = map.get(currency);

        if(current == null)
            current = Money.of(0);

        map.put(currency, current.plus(money));
    }
}
