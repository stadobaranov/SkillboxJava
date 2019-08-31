import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class ImageDownloader {
    private static final int BUFFER_SIZE = 8192;

    private final String url;
    private final String folderPath;
    private final Function<String, String> nameGenerator;
    private final byte buffer[];

    public ImageDownloader(String url, String folderPath, Function<String, String> nameGenerator) {
        this.url = url;
        this.folderPath = folderPath;
        this.nameGenerator = nameGenerator;
        this.buffer = new byte[BUFFER_SIZE];
    }

    public void download() throws IOException {
        Document document = Jsoup.connect(url).get();

        for(Element element: document.select("img"))
            downloadImage(element);
    }

    private void downloadImage(Element image) throws IOException {
        String src = image.absUrl("src");
        String dst = folderPath + File.separator + nameGenerator.apply(src);
        URL url = new URL(src);

        try(InputStream is = new BufferedInputStream(url.openStream(), BUFFER_SIZE)) {
            try(OutputStream os = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE)) {
                for(;;) {
                    int bytes = is.read(buffer);

                    if(bytes < 0) {
                        break;
                    }

                    os.write(buffer, 0, bytes);
                }

                os.flush();
            }
        }
    }

    public static void main(String... args) throws IOException {
        ImageDownloader downloader = new ImageDownloader(
            "https://www.lenta.ru",
            "downloaded-images",
            ImageDownloader::encodeFileName
        );

        downloader.download();
    }

    private static String encodeFileName(String path) {
        return URLEncoder.encode(path, StandardCharsets.UTF_8);
    }
}
