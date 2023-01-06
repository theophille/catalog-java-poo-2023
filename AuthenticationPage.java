import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AuthenticationPage extends JFrame implements ActionListener {
    private JTextField userName;
    private JTextField password;
    private JButton authButton;
    private ArrayList<Account> accounts;

    public AuthenticationPage() throws FileNotFoundException {
        super("Catalog");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 450));

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        setLocation(new Point(600, 400));

        UsersDatabase database = new UsersDatabase(new File("D:\\School Stuff\\UPB ACS\\Anul II - 2022\\Tema\\TemaPOO\\src\\users.txt"));
        accounts = database.getAccounts();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        JLabel pageTitle = new JLabel("Authenticate");
        pageTitle.setFont(new Font("Poppins", Font.BOLD, 16));
        add(pageTitle, constraints);

        JPanel loginArea = new JPanel();

        JPanel userNameField = new JPanel();
        JLabel userNameLabel = new JLabel("Full Name:");
        userName = new JTextField();
        userName.setPreferredSize(new Dimension(150, 20));
        userNameField.add(userNameLabel);
        userNameField.add(userName);

        JPanel passwordField = new JPanel();
        JLabel passwordLabel = new JLabel("Password:");
        password = new JTextField();
        password.setPreferredSize(new Dimension(150, 20));
        passwordField.add(passwordLabel);
        passwordField.add(password);

        authButton = new JButton("Login");
        authButton.addActionListener(this);

        loginArea.add(userNameField);
        loginArea.add(passwordField);

        constraints.gridy = 1;
        add(loginArea, constraints);

        constraints.gridy = 2;
        add(authButton, constraints);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Account compAccount = new Account("", userName.getText(), password.getText());
        int accountLocation = accounts.indexOf(compAccount);
        if(accountLocation >= 0) {
            Account searchedAccount = accounts.get(accountLocation);
            WindowFactory windowFactory = new WindowFactory();
            try {
                App.activeWindow = windowFactory.getWindow(searchedAccount.getUserType(), searchedAccount);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Wrong credentials. Try again!");
        }
    }
}
