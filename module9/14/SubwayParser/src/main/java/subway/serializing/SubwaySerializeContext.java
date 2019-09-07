package subway.serializing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import subway.Connection;
import subway.Line;
import subway.Station;
import subway.Subway;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class SubwaySerializeContext {
    public void serialize(Subway subway, String path) {
        JSONObject root = toJson(subway);

        try(BufferedWriter out = new BufferedWriter(new FileWriter(path))) {
            root.writeJSONString(out);
        }
        catch(IOException exception) {
            throw new SubwaySerializerException("Не удалось записать json-файл", exception);
        }
    }

    private static JSONObject toJson(Subway subway) {
        JSONObject root = new JSONObject();

        root.put("lines", serializeLines(subway.getLines()));
        root.put("stations", serializeStations(subway.getStations()));
        root.put("connections", serializeConnections(subway.getConnections()));

        return root;
    }

    private static JSONArray serializeLines(Collection<Line> lines) {
        JSONArray serializedLines = new JSONArray();

        for(Line line: lines) {
            JSONObject serializedLine = new JSONObject();

            serializedLine.put("number", line.getNumber());
            serializedLine.put("name", line.getName());

            serializedLines.add(serializedLine);
        }

        return serializedLines;
    }

    private static JSONObject serializeStations(Collection<Station> stations) {
        JSONObject serializedStations = new JSONObject();

        for(Station station: stations) {
            String lineNumber = station.getLine().getNumber();

            JSONArray lineStations = (JSONArray)serializedStations.computeIfAbsent(
                lineNumber, (key) -> new JSONArray()
            );

            lineStations.add(station.getName());
        }

        return serializedStations;
    }

    private static JSONArray serializeConnections(Collection<Connection> connections) {
        JSONArray serializedConnections = new JSONArray();

        for(Connection connection: connections) {
            JSONArray serializedConnectedStations = new JSONArray();

            for(Station station: connection.getStations()) {
                JSONObject serializedStation = new JSONObject();

                serializedStation.put("line", station.getLine().getNumber());
                serializedStation.put("station", station.getName());

                serializedConnectedStations.add(serializedStation);
            }

            serializedConnections.add(serializedConnectedStations);
        }

        return serializedConnections;
    }
}
