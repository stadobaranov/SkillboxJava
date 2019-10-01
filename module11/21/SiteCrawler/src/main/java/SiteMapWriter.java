import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class SiteMapWriter {
    public void write(File file, Set<URL> urls) {
        if(file.exists()) {
            if(!file.isFile()) {
                throw new SiteMapWriterException(
                    String.format("Путь \"%s\" не является файлом", file));
            }
        }
        else {
            file.getParentFile().mkdirs();
        }

        SiteMapNode root = buildSiteMap(urls);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            write(writer, 0, "", root);
        }
        catch(IOException exception) {
            throw new SiteMapWriterException(
                String.format("Не удалось записать карту сайта в файл \"%s\"", file), exception);
        }
    }

    private static void write(BufferedWriter writer, int level, String key, SiteMapNode node) throws IOException {
        URL url = node.getUrl();

        String out = url != null
            ? url.toString()
            : "/* Нет ссылок на страницу \"" + key + "\" */";

        writer.write("\t".repeat(level) + out + "\n");

        for(Map.Entry<String, SiteMapNode> entry: node.entrySet()) {
            write(writer, level + 1, entry.getKey(), entry.getValue());
        }
    }

    private static SiteMapNode buildSiteMap(Set<URL> urls) {
        SiteMapNode root = new SiteMapNode();

        for(URL url: urls) {
            String path = url.getPath();

            if(path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            SiteMapNode node = root;
            String parts[] = path.split("/");

            for(int i = 1; i < parts.length; i++) {
                String part = parts[i];
                SiteMapNode child = node.get(part);

                if(child == null) {
                    node.put(part, child = new SiteMapNode());
                }

                node = child;
            }

            node.setUrl(url);
        }

        return root;
    }
}
