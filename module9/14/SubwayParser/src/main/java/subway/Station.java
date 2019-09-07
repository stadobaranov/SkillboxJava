package subway;

import java.util.Objects;

public class Station {
    private final String name;
    private final Line line;

    public Station(String name, Line line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), line);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Station) {
            Station otherStation = (Station)other;
            return name.equalsIgnoreCase(otherStation.name) && line.equals(otherStation.line);
        }

        return false;
    }
}
