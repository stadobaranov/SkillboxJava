import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.CardLayout;

public class ApplicationForm {
    private JPanel rootPanel;
    private JButton collapseButton;
    private JButton expandButton;
    private JTextField firstNameTextField;
    private JTextField middleNameTextField;
    private JTextField lastNameTextField;
    private JTextField fullNameTextField;

    private ApplicationForm() {}

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void initialize() {
        collapseButton.addActionListener(event -> tryCollapseName());
        expandButton.addActionListener(event -> tryExpandName());
    }

    private void tryCollapseName() {
        String firstName = firstNameTextField.getText().trim();
        String middleName = middleNameTextField.getText().trim();
        String lastName = lastNameTextField.getText().trim();

        if(firstName.isEmpty() || lastName.isEmpty()) {
            showValidationError("First name and last name fields is required");
            return;
        }

        StringBuilder fullName = new StringBuilder();
        fullName.append(firstName);

        if(!middleName.isEmpty()) {
            fullName.append(' ');
            fullName.append(middleName);
        }

        fullName.append(' ');
        fullName.append(lastName);
        fullNameTextField.setText(fullName.toString());

        changeStateTo(State.COLLAPSED);
    }

    private void tryExpandName() {
        String nameParts[] = fullNameTextField.getText().split("\\s+");

        if(nameParts.length == 2) {
            firstNameTextField.setText(nameParts[0]);
            middleNameTextField.setText("");
            lastNameTextField.setText(nameParts[1]);
        }
        else if(nameParts.length == 3) {
            firstNameTextField.setText(nameParts[0]);
            middleNameTextField.setText(nameParts[1]);
            lastNameTextField.setText(nameParts[2]);
        }
        else {
            showValidationError("Invalid full name format");
            return;
        }

        changeStateTo(State.EXPANDED);
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(rootPanel, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void changeStateTo(State state) {
        ((CardLayout)rootPanel.getLayout()).show(rootPanel, state.name());
    }

    public static ApplicationForm create() {
        ApplicationForm form = new ApplicationForm();
        form.initialize();
        return form;
    }

    private static enum State {
        COLLAPSED, EXPANDED
    }
}
