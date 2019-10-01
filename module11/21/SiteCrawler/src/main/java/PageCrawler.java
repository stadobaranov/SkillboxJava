import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

public class PageCrawler extends RecursiveTask<Set<URL>> {
    private final URL url;
    private final Set<String> handledPaths;

    private PageCrawler(URL url, Set<String> handledPaths) {
        this.url = url;
        this.handledPaths = handledPaths;
    }

    @Override
    protected Set<URL> compute() {
        Set<String> links;

        try {
            PageLinkParser parser = new PageLinkParser();
            links = parser.parseLinks(url);
        }
        catch(PageNotParsedException exception) {
            System.out.println(exception.getMessage());
            links = null;
        }

        if(links != null && !links.isEmpty()) {
            List<PageCrawler> crawlers = new ArrayList<>();

            for(String link: links) {
                URL linkUrl;

                try {
                    linkUrl = new URL(link);
                }
                catch(MalformedURLException exception) {
                    System.out.printf("Ссылка \"%s\" не является валидным URL%n", link);
                    continue;
                }

                String host = url.getHost();
                String linkHost = linkUrl.getHost();

                if(!host.equals(linkHost)) {
                    continue;
                }

                String linkPath = appendSlash(linkUrl.getPath());

                if(!handledPaths.add(linkPath)) {
                    continue;
                }

                PageCrawler crawler = new PageCrawler(linkUrl, handledPaths);
                crawlers.add(crawler);
                crawler.fork();
            }

            if(!crawlers.isEmpty()) {
                Set<URL> result = new HashSet<>();
                result.add(url);

                for(PageCrawler crawler: crawlers) {
                    result.addAll(crawler.join());
                }

                return result;
            }
        }

        return Collections.singleton(url);
    }

    public static PageCrawler of(URL url) {
        Set<String> handledPaths = Collections.newSetFromMap(new ConcurrentHashMap<>());
        handledPaths.add(appendSlash(url.getPath()));
        return new PageCrawler(url, handledPaths);
    }

    private static String appendSlash(String path) {
        return path.endsWith("/")? path: path + "/";
    }
}
