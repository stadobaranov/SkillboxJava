package subway;

import java.util.Collection;
import java.util.Collections;

public class Subway {
    private final Collection<Line> lines;
    private final Collection<Station> stations;
    private final Collection<Connection> connections;

    public Subway(Collection<Line> lines, Collection<Station> stations, Collection<Connection> connections) {
        this.lines = lines;
        this.stations = stations;
        this.connections = connections;
    }

    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(lines);
    }

    public Collection<Station> getStations() {
        return Collections.unmodifiableCollection(stations);
    }

    public Collection<Connection> getConnections() {
        return Collections.unmodifiableCollection(connections);
    }
}
