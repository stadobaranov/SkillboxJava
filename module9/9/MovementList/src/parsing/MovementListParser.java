package parsing;

import core.Currency;
import core.Money;
import core.MovementRecord;
import core.MovementType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovementListParser {
    private static String joinCurrencyNames(String delimiter) {
        return Arrays.stream(Currency.values())
                     .map(Currency::name)
                     .collect(Collectors.joining(delimiter));
    }

    private static final Pattern descriptionPattern = Pattern.compile(
        "(?:\\d{6}\\+{6}\\d{4})" + // Номер карты
        "(?<serviceInfo>.{54})" + // Информация услуги
        "(?:(?:\\d{2}\\.\\d{2}\\.\\d{2} ){2})" + // Даты (совершения и прохождения???)
        "(?<sum>.{12}) {2}" + // Сумма
        "(?<currency>" + joinCurrencyNames("|") + ")" + // Валюта
        "(?:.*)" // Информация платежной системы
    );

    private static final Pattern sumPattern = Pattern.compile(
        "(?<integerPart>0|(?:[1-9]\\d*))\\.(?<fractionPart>\\d{2})"
    );

    public Stream<MovementRecord> parsedRecords(String path) {
        try {
            return Files.lines(Path.of(path))
                        .skip(1)
                        .map(MovementListParser::parseRecord);
        }
        catch(IOException exception) {
            throw new MovementListParseException("Не удалось разобрать файл с выпиской", exception);
        }
    }

    public List<MovementRecord> parse(String path) {
        return parsedRecords(path).collect(Collectors.toList());
    }

    private static MovementRecord parseRecord(String raw) {
        String columns[] = raw.split(",", 6);

        if(columns.length != 6) {
            throw new MovementListParseException(
                String.format("Не удалось разобрать строку операции - \"%s\"", raw));
        }

        Matcher descriptionMatcher = descriptionPattern.matcher(columns[5]);

        if(!descriptionMatcher.matches()) {
            throw new MovementListParseException(
                String.format("Не удалось разобрать описание операции - \"%s\"", columns[5]));
        }

        String rawServiceInfo = descriptionMatcher.group("serviceInfo");
        String rawSum = descriptionMatcher.group("sum");
        String rawCurrency = descriptionMatcher.group("currency");

        return new MovementRecord(
            parseServiceName(rawServiceInfo.trim()),
            rawSum.startsWith(" ")? MovementType.EXPENSE: MovementType.INCOME,
            parseSum(rawSum.trim()),
            Currency.valueOf(rawCurrency)
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

    private static Money parseSum(String raw) {
        Matcher matcher = sumPattern.matcher(raw);

        if(!matcher.matches()) {
            throw new MovementListParseException(
                String.format("Недопустимый формат суммы денежных средств - \"%s\"", raw));
        }

        return Money.of(
            Long.parseLong(matcher.group("integerPart")) * 100 +
            Byte.parseByte(matcher.group("fractionPart"))
        );
    }
}
