package subway.building;

import java.util.Objects;

public class StationDef {
    private final String name;
    private final String lineNumber;

    public StationDef(String name, String lineNumber) {
        this.name = name;
        this.lineNumber = lineNumber;
    }

    public String getName() {
        return name;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), lineNumber.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof StationDef) {
            StationDef otherStation = (StationDef)other;
            return name.equalsIgnoreCase(otherStation.name) && lineNumber.equalsIgnoreCase(otherStation.lineNumber);
        }

        return false;
    }
}
