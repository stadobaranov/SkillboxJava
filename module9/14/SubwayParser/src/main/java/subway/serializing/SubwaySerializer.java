package subway.serializing;

import subway.Subway;

public class SubwaySerializer {
    public void serialize(Subway subway, String path) {
        SubwaySerializeContext context = new SubwaySerializeContext();
        context.serialize(subway, path);
    }

    public Subway deserialize(String path) {
        SubwayDeserializeContext context = new SubwayDeserializeContext();
        return context.deserialize(path);
    }
}
