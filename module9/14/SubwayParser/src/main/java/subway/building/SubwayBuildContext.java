package subway.building;

import subway.Connection;
import subway.Line;
import subway.Station;
import subway.Subway;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class SubwayBuildContext {
    private final Consumer<String> errorMessageConsumer;
    private final Map<String, Line> indexedLines;
    private final Map<String, Map<String, Station>> indexedStations;
    private final Map<Station, Set<Station>> indexedConnections;

    public SubwayBuildContext(Consumer<String> errorMessageConsumer) {
        this.errorMessageConsumer = errorMessageConsumer;
        this.indexedLines = new HashMap<>();
        this.indexedStations = new LinkedHashMap<>();
        this.indexedConnections = new HashMap<>();
    }

    public void addLine(LineDef def) {
        String lineNumber = def.getNumber();
        String lineName = def.getName();
        String lineKey = lineNumber.toLowerCase();
        Line existingLine = indexedLines.get(lineKey);

        if(existingLine == null) {
            indexedLines.put(lineKey, new Line(lineNumber, lineName));
        }
        else {
            String existingLineName = existingLine.getName();

            if(!lineName.equalsIgnoreCase(existingLineName)) {
                errorMessageConsumer.accept(
                    String.format(
                        "Дублирование номера ветки \"%s\" для имен: \"%s\" и \"%s\"",
                        lineNumber,
                        lineName,
                        existingLineName
                    )
                );
            }
        }
    }

    public void addStation(StationDef def) {
        String stationName = def.getName();
        String stationLineNumber = def.getLineNumber();
        String lineKey = stationLineNumber.toLowerCase();
        Line existingLine = indexedLines.get(lineKey);

        if(existingLine == null) {
            errorMessageConsumer.accept(
                String.format(
                    "Станция \"%s\" находится на несуществующей ветке с номером \"%s\"",
                    stationName,
                    stationLineNumber
                )
            );
        }
        else {
            String stationKey = stationName.toLowerCase();

            Map<String, Station> lineStations = indexedStations.computeIfAbsent(
                lineKey, (key) -> new LinkedHashMap<>()
            );

            lineStations.put(stationKey, new Station(stationName, existingLine));
        }
    }

    public void addConnection(ConnectionDef def) {
        Set<StationDef> stationDefs = def.getStations();

        if(stationDefs.size() > 1) {
            Set<Station> stations = new HashSet<>();
            Set<Line> stationLines = new HashSet<>();

            for(StationDef stationDef: stationDefs) {
                String stationName = stationDef.getName();
                String stationLineNumber = stationDef.getLineNumber();
                String lineKey = stationLineNumber.toLowerCase();
                Map<String, Station> lineStations = indexedStations.get(lineKey);

                if(lineStations == null) {
                    errorMessageConsumer.accept(
                        String.format(
                            "Станция пересадки \"%s\" находится на несуществующей ветке с номером \"%s\"",
                            stationName,
                            stationLineNumber
                        )
                    );

                    return;
                }

                String stationKey = stationName.toLowerCase();
                Station station = lineStations.get(stationKey);

                if(station == null) {
                    errorMessageConsumer.accept(
                        String.format(
                            "Станция пересадки \"%s\" на ветке с номером \"%s\" не существует",
                            stationName,
                            stationLineNumber
                        )
                    );

                    return;
                }

                if(!stationLines.add(station.getLine())) {
                    errorMessageConsumer.accept("Станции с пересадками должны находится на разных линиях");
                    return;
                }

                stations.add(station);
            }

            for(Station station: stations) {
                Set<Station> connectedStations = indexedConnections.computeIfAbsent(
                    station, (key) -> new HashSet<>()
                );

                connectedStations.addAll(stations);
            }
        }
    }

    public Subway build() {
        List<Station> stations = new ArrayList<>();
        Set<Connection> connections = new HashSet<>();

        for(Map.Entry<String, Map<String, Station>> lineEntry: indexedStations.entrySet()) {
            Map<String, Station> lineStations = lineEntry.getValue();

            for(Map.Entry<String, Station> stationEntry: lineStations.entrySet()) {
                stations.add(stationEntry.getValue());
            }
        }

        for(Map.Entry<Station, Set<Station>> connectionEntry: indexedConnections.entrySet()) {
            connections.add(new Connection(connectionEntry.getValue()));
        }

        return new Subway(indexedLines.values(), stations, connections);
    }
}
