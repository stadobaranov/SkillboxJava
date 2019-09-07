package subway.serializing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import subway.Subway;
import subway.building.ConnectionDef;
import subway.building.LineDef;
import subway.building.StationDef;
import subway.building.SubwayBuilder;
import subway.building.SubwayBuilderException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubwayDeserializeContext {
    public Subway deserialize(String path) {
        Object raw;

        try(BufferedReader in = new BufferedReader(new FileReader(path))) {
            JSONParser parser = new JSONParser();

            try {
                raw = parser.parse(in);
            }
            catch(ParseException parseException) {
                throw new SubwaySerializerException("Не удалось разобрать json-файл", parseException);
            }
        }
        catch(IOException ioException) {
            throw new SubwaySerializerException("Не удалось прочитать json-файл", ioException);
        }

        return build(tryCast(raw, JSONObject.class));
    }

    private static Subway build(JSONObject root) {
        JSONArray lines = tryCast(root.get("lines"), JSONArray.class, false);
        JSONObject stations = tryCast(root.get("stations"), JSONObject.class, false);
        JSONArray connections = tryCast(root.get("connections"), JSONArray.class, false);

        SubwayBuilder builder = new SubwayBuilder();

        if(lines != null) {
            buildLines(lines, builder);
        }

        if(stations != null) {
            buildStations(stations, builder);
        }

        if(connections != null) {
            buildConnections(connections, builder);
        }

        try {
            return builder.build();
        }
        catch(SubwayBuilderException exception) {
            throw new SubwaySerializerException("Не удалось десерилизовать метрополитен", exception);
        }
    }

    private static void buildLines(JSONArray lines, SubwayBuilder builder) {
        for(int i = 0; i < lines.size(); i++) {
            JSONObject line = tryCast(lines.get(i), JSONObject.class);
            String number = tryCast(line.get("number"), String.class);
            String name = tryCast(line.get("name"), String.class);
            builder.addLine(new LineDef(number, name));
        }
    }

    private static void buildStations(JSONObject stations, SubwayBuilder builder) {
        @SuppressWarnings("unchecked")
        Set<Map.Entry> entrySet = stations.entrySet();

        for(Map.Entry entry: entrySet) {
            String lineNumber = tryCast(entry.getKey(), String.class);
            JSONArray names = tryCast(entry.getValue(), JSONArray.class);

            for(int i = 0; i < names.size(); i++) {
                builder.addStation(new StationDef(tryCast(names.get(i), String.class), lineNumber));
            }
        }
    }

    private static void buildConnections(JSONArray connections, SubwayBuilder builder) {
        for(int i = 0; i < connections.size(); i++) {
            Set<StationDef> stationDefs = new HashSet<>();
            JSONArray connectedStations = tryCast(connections.get(i), JSONArray.class);

            for(int j = 0; j < connectedStations.size(); j++) {
                JSONObject connectedStation = tryCast(connectedStations.get(j), JSONObject.class);
                String line = tryCast(connectedStation.get("line"), String.class);
                String station = tryCast(connectedStation.get("station"), String.class);
                stationDefs.add(new StationDef(station, line));
            }

            builder.addConnection(new ConnectionDef(stationDefs));
        }
    }

    private static <T> T tryCast(Object raw, Class<? extends T> type) {
        return tryCast(raw, type, true);
    }

    private static <T> T tryCast(Object raw, Class<? extends T> type, boolean required) {
        if(required && raw == null) {
            throw new SubwaySerializerException("Недопустимый формат json-файла");
        }

        T casted;

        try {
            casted = type.cast(raw);
        }
        catch(ClassCastException exception) {
            throw new SubwaySerializerException("Недопустимый формат json-файла");
        }

        return casted;
    }
}
