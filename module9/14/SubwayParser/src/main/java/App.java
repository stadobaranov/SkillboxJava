import subway.Line;
import subway.Station;
import subway.Subway;
import subway.parsing.SubwayParser;
import subway.serializing.SubwaySerializer;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class App {
    private static String URL_PATH = "https://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена";
    private static String FILE_PATH = "src/main/resources/subway.json";

    public static void main(String... args) {
        SubwayParser parser = new SubwayParser();
        Subway parsedSubway = parser.parse(URL_PATH);

        SubwaySerializer serializer = new SubwaySerializer();
        serializer.serialize(parsedSubway, FILE_PATH);

        Subway deserializedSubway = serializer.deserialize(FILE_PATH);

        // System.out.println(compareCollections(parsedSubway.getLines(), deserializedSubway.getLines()));
        // System.out.println(compareCollections(parsedSubway.getStations(), deserializedSubway.getStations()));
        // System.out.println(compareCollections(parsedSubway.getConnections(), deserializedSubway.getConnections()));

        Map<Line, Integer> lineStations = new TreeMap<>(Comparator.comparing(Line::getNumber));

        for(Station station: deserializedSubway.getStations()) {
            Line line = station.getLine();
            Integer stations = lineStations.get(line);
            lineStations.put(line, (stations != null? stations: 0) + 1);
        }

        for(Map.Entry<Line, Integer> entry: lineStations.entrySet()) {
            System.out.printf("(%4s) %s: %d%n", entry.getKey().getNumber(), entry.getKey().getName(), entry.getValue());
        }
    }

    // private static <T> boolean compareCollections(Collection<T> a, Collection<T> b) {
    //     if(a.size() != b.size()) {
    //         return false;
    //     }
    //
    //     return new HashSet<>(a).equals(new HashSet<>(b));
    // }
}
