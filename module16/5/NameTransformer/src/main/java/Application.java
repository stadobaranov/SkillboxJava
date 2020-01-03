import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;

public class Application {
    public static void main(String... args) {
        JFrame frame = new JFrame("NameTransformer v0.1a");
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ApplicationForm form = ApplicationForm.create();
        frame.add(form.getRootPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
