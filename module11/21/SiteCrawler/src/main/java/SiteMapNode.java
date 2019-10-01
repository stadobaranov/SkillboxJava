import java.net.URL;
import java.util.TreeMap;

public class SiteMapNode extends TreeMap<String, SiteMapNode> {
    private URL url;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
