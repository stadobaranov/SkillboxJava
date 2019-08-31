import util.Currency;
import util.Money;
import util.MoneyFormatException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovementListParser {
    private static final String CURRENCIES;

    static {
        StringJoiner joiner = new StringJoiner("|");

        for(Currency currency: Currency.values()) {
            joiner.add(currency.name());
        }

        CURRENCIES = joiner.toString();
    }

    private static final Pattern descriptionPattern = Pattern.compile(
        "(?:\\d{6}\\+{6}\\d{4})" + // Номер карты
        "(.{54})" + // Информация услуги
        "(?:(?:\\d{2}\\.\\d{2}\\.\\d{2} ){2})" + // Даты (совершения и прохождения???)
        "(.{12}) {2}" + // Баланс
        "(" + CURRENCIES + ")" + // Валюта
        "(?:.*)" // Информация платежной системы
    );

    public MovementList parse(String path) {
        MovementList list = new MovementList();

        try {
            Files.lines(Path.of(path))
                 .skip(1)
                 .map(this::parseItem)
                 .forEach(list::addItem);
        }
        catch(IOException exception) {
            throw new MovementListParseException("Не удалось разобрать файл с выпиской", exception);
        }

        return list;
    }

    private MovementListItem parseItem(String raw) {
        String columns[] = raw.split(",", 6);

        if(columns.length != 6) {
            throw new MovementListParseException("Не удалось разобрать строку с операцией");
        }

        Matcher descriptionMatcher = descriptionPattern.matcher(columns[5]);

        if(!descriptionMatcher.matches()) {
            throw new MovementListParseException("Не удалось разобрать описание операции");
        }

        String group1 = descriptionMatcher.group(1);
        String group2 = descriptionMatcher.group(2);
        String group3 = descriptionMatcher.group(3);

        return new MovementListItem(
            parseServiceName(group1.trim()),
            group2.startsWith(" ")? MovementType.CREDIT: MovementType.DEBIT,
            parseMoney(group2.trim()),
            Currency.valueOf(group3)
        );
    }

    private static String parseServiceName(String info) {
        int start = 0;
        int end = info.length();

        for(int i = end - 1; i >= 0; i--) {
            char c = info.charAt(i);

            if(c == '\\' || c == '/') {
                start = i + 1;
                break;
            }
            else if(c == '>') {
                end = i;
            }
        }

        return info.substring(start, end).trim();
    }

    private static Money parseMoney(String raw) {
        try {
            return Money.parse(raw);
        }
        catch(MoneyFormatException exception) {
            throw new MovementListParseException("Не удалось разобрать сумму прихода/расхода", exception);
        }
    }
}
