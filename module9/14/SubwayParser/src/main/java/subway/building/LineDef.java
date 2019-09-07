package subway.building;

import java.util.Objects;

public class LineDef {
    private final String number;
    private final String name;

    public LineDef(String number, String name) {
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
        if(other instanceof LineDef) {
            LineDef otherLine = (LineDef)other;
            return number.equalsIgnoreCase(otherLine.number) && name.equalsIgnoreCase(otherLine.name);
        }

        return false;
    }
}
