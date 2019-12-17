package vote;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Voter {
    private static final DateTimeFormatter birthDayFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final String name;
    private final LocalDate birthDay;

    public Voter(String name, String birthDay) {
        this(name, parseBirthDay(birthDay));
    }

    public Voter(String name, LocalDate birthDay) {
        this.name = name;
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthDay);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Voter) {
            Voter otherVoter = (Voter)other;
            return name.equals(otherVoter.name) && birthDay.equals(otherVoter.birthDay);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, birthDay.format(birthDayFormatter));
    }

    private static LocalDate parseBirthDay(String birthDay) {
        try {
            return LocalDate.parse(birthDay, birthDayFormatter);
        }
        catch(DateTimeParseException exception) {
            throw new BirthDayFormatException("Невалидный формат даты рождения", exception);
        }
    }
}
