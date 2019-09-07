package subway;

import java.util.Collections;
import java.util.Set;

public class Connection {
    private final Set<Station> stations;

    public Connection(Set<Station> stations) {
        this.stations = stations;
    }

    public Set<Station> getStations() {
        return Collections.unmodifiableSet(stations);
    }

    @Override
    public int hashCode() {
        return stations.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Connection && stations.equals(((Connection)other).stations);
    }
}
