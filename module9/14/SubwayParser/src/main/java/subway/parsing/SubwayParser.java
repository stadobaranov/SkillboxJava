package subway.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import subway.Subway;
import subway.building.ConnectionDef;
import subway.building.LineDef;
import subway.building.StationDef;
import subway.building.SubwayBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SubwayParser {
    public Subway parse(String urlPath) {
        Document document;

        // try {
        //     // Читает "обрезаную" версию страницы педивикии.
        //     document = Jsoup.connect(urlPath).get();
        // }
        // catch(IOException exception) {
        //     throw new SubwayParserException("Не удалось загрузить документ", exception);
        // }

        try {
            URL url = new URL(urlPath);

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;

                for(;;) {
                    line = reader.readLine();

                    if(line == null) {
                        break;
                    }

                    sb.append(line);
                }

                document = Jsoup.parse(
                    sb.toString(),
                    String.format("%s://%s", url.getProtocol(), url.getHost())
                );
            }
        }
        catch(IOException exception) {
            throw new SubwayParserException("Не удалось загрузить документ", exception);
        }

        return parseDocument(document);
    }

    private Subway parseDocument(Document document) {
        // На странице вики с московским метрополитеном на момент написания, в столбце пересадок было несколько
        // неточностей:
        //
        // (1) Для станции "Шелепиха" (Московское центральное кольцо, номер 14) указана пересадка
        // на одноименную станцию Солнцевской и Большой кольцевой линии (общая станция), но извлечь название
        // станции для Солнцевской линии не предоставляется возможным, т.к. ссылка в столбце указывает на
        // страницу с веткой, а не станцию.
        //
        // (2) Для станции "Хорошёво" (Московское центральное кольцо, номер 14) указана пересадка
        // на станцию "Хорошёвская" Солнцевской и Большой кольцевой линии (общая станция), но извлечь название
        // станции для Солнцевской линии не предоставляется возможным, т.к. ссылка в столбце указывает на
        // страницу с веткой, а не станцию.
        //
        // (3) Для станции "Выставочная" (на Филевской линии, с номером 4) есть пересадка на станцию
        // "Деловой центр", которая указана как станция Калининской линии, а по факту она находится
        // на Солнцевкой линии.
        //
        // (4) Для станции "Парк Победы" (на Арбатско-Покровской линии, с номером 3) есть пересадка на одноименную
        // станцию, которая указана как станция Калининской линии, а по факту она находится
        // на Солнцевкой линии.
        //
        // Для подавления последних двух проблем, билдер в этом методе не кидает исключения, а тупо выводит
        // варнинг на консоль.
        SubwayBuilder builder = new SubwayBuilder(SubwayParser::printBuildingError);

        for(Element tableRow: document.select(".mw-parser-output table.standard tr")) {
            Elements tableCells = tableRow.select("td");

            if(tableCells.size() >= 4) {
                Set<StationDef> connectedStationDefs = new HashSet<>();

                // Костыль для решения проблем (1) и (2) - добавления дополнительного селектора по атрибуту title
                // для пересадок.
                for(Element connectionAnchor: tableCells.get(3).select("a[title*=станци]")) {
                    Element lineNumberSpan = connectionAnchor.parent().previousElementSibling();

                    StationDef stationDef = new StationDef(
                        extractNameFromUri(connectionAnchor.absUrl("href")),
                        lineNumberSpan.text().trim()
                    );

                    connectedStationDefs.add(stationDef);
                }

                String stationName = tableCells.get(1).selectFirst("a").text().trim();

                // Станции: "Шелепиха", "Хорошёвская", "ЦСКА", "Петровский парк", "Савёловская", общие для
                // Солнцевской (8А) и Большой кольцевой линии (11) будут добавлены для каждой ветки отдельно,
                // и между ними будет добавлена пересадка.
                for(Element lineAnchor: tableCells.get(0).select("a")) {
                    Element lineNumberSpan = lineAnchor.parent().previousElementSibling();
                    String lineNumber = lineNumberSpan.text().trim();

                    StationDef stationDef = new StationDef(stationName, lineNumber);
                    builder.addStation(stationDef);
                    connectedStationDefs.add(stationDef);

                    builder.addLine(new LineDef(
                        lineNumber, extractNameFromUri(lineAnchor.absUrl("href"))
                    ));
                }

                if(connectedStationDefs.size() > 1) {
                    builder.addConnection(new ConnectionDef(connectedStationDefs));
                }
            }
        }

        return builder.build();
    }

    private static String extractNameFromUri(String uri) {
        int lastSlashPos = uri.lastIndexOf('/');

        if(lastSlashPos != -1) {
            uri = uri.substring(lastSlashPos + 1);
        }

        int firstBracketPos = uri.indexOf('(');

        if(firstBracketPos != -1) {
            uri = uri.substring(0, firstBracketPos);
        }

        return URLDecoder.decode(uri, StandardCharsets.UTF_8)
                         .replaceAll("_", " ")
                         .trim();
    }

    private static void printBuildingError(String message) {
        System.out.printf("При разборе метрополитена возникла ошибка: %s%n", message);
    }
}
