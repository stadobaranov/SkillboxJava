import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class SiteCrawler {
    private final ForkJoinPool pool;

    public SiteCrawler(ForkJoinPool pool) {
        this.pool = pool;
    }

    public Set<URL> crawl(URL url) {
        PageCrawler crawler = PageCrawler.of(url);
        return pool.invoke(crawler);
    }

    public static void main(String... args) throws MalformedURLException {
        SiteCrawler crawler = new SiteCrawler(ForkJoinPool.commonPool());
        Set<URL> urls = crawler.crawl(new URL("https://skillbox.ru"));

        SiteMapWriter writer = new SiteMapWriter();
        writer.write(new File("src/main/resources/sitemap"), urls);
    }
}
