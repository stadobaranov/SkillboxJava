import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopyTask implements Runnable {
    private static final int BUFFER_SIZE = 8192;

    private final File src;
    private final File dst;

    public FileCopyTask(File src, File dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    public void run() {
        try {
            try(BufferedInputStream is = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE)) {
                try(BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE)) {
                    byte buffer[] = new byte[BUFFER_SIZE];

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
        catch(IOException exception) {
            throw new DirectoryCopyException(
                String.format(
                    "Не удалось копировать файл \"%s\" в \"%s\"", src.getAbsolutePath(), dst.getAbsolutePath()
                ),

                exception
            );
        }
    }
}
