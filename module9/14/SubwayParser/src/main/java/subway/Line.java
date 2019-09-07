package subway;

import java.util.Objects;

public class Line {
    private final String number;
    private final String name;

    public Line(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number.toLowerCase(), name.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Line) {
            Line otherLine = (Line)other;
            return number.equalsIgnoreCase(otherLine.number) && name.equalsIgnoreCase(otherLine.name);
        }

        return false;
    }
}
