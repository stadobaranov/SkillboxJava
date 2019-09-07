package subway.building;

import subway.Subway;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SubwayBuilder {
    private final Consumer<String> errorMessageConsumer;
    private final List<LineDef> lineDefs;
    private final List<StationDef> stationDefs;
    private final List<ConnectionDef> connectionDefs;

    public SubwayBuilder() {
        this(SubwayBuilder::throwException);
    }

    public SubwayBuilder(Consumer<String> errorMessageConsumer) {
        this.errorMessageConsumer = errorMessageConsumer;
        this.lineDefs = new ArrayList<>();
        this.stationDefs = new ArrayList<>();
        this.connectionDefs = new ArrayList<>();
    }

    public void addLine(LineDef def) {
        lineDefs.add(def);
    }

    public void addStation(StationDef def) {
        stationDefs.add(def);
    }

    public void addConnection(ConnectionDef def) {
        connectionDefs.add(def);
    }

    public Subway build() {
        SubwayBuildContext context = new SubwayBuildContext(errorMessageConsumer);

        for(LineDef lineDef: lineDefs) {
            context.addLine(lineDef);
        }

        for(StationDef stationDef: stationDefs) {
            context.addStation(stationDef);
        }

        for(ConnectionDef connectionDef: connectionDefs) {
            context.addConnection(connectionDef);
        }

        return context.build();
    }

    private static void throwException(String message) {
        throw new SubwayBuilderException(message);
    }
}
