package subway.building;

import java.util.Collections;
import java.util.Set;

public class ConnectionDef {
    private final Set<StationDef> stations;

    public ConnectionDef(Set<StationDef> stations) {
        this.stations = stations;
    }

    public Set<StationDef> getStations() {
        return Collections.unmodifiableSet(stations);
    }

    @Override
    public int hashCode() {
        return stations.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ConnectionDef && stations.equals(((ConnectionDef)other).stations);
    }
}
