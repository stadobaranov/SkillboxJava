import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

public class PageLinkParser {
    public Set<String> parseLinks(URL url) {
        Document document;

        try {
            document = Jsoup.connect(url.toString())
                            .maxBodySize(0)
                            .get();
        }
        catch(IOException exception) {
            throw new PageNotParsedException(
                String.format("Не удалось разобрать страницу по адресу: %s", url));
        }

        return parseDocument(document);
    }

    private Set<String> parseDocument(Document document) {
        Set<String> links = new TreeSet<>();

        for(Element element: document.select("a[href]")) {
            links.add(element.absUrl("href"));
        }

        return links;
    }
}
