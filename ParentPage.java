import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class ParentPage extends JFrame {
    JList<Notification> notList;
    public ParentPage(Account account) {
        super("Welcome " + account.getUserName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 450));
        setLocation(new Point(600, 400));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        StringTokenizer tokenizer = new StringTokenizer(account.getUserName(), " ");
        String firstName = tokenizer.nextToken();
        String lastName = tokenizer.nextToken();
        Parent parent = new Parent(firstName, lastName);
        int index = Catalog.getInstance().getObservers().indexOf(parent);
        Parent searchedParent = (Parent) Catalog.getInstance().getObservers().get(index);
        Vector<Notification> not = new Vector<>(searchedParent.getNotifications());
        notList = new JList<>(not);
        JScrollPane scrollableList = new JScrollPane(notList);
        scrollableList.setBorder(new TitledBorder("New Notifications"));
        add(scrollableList, constraints);

        pack();
        setVisible(true);
    }
}
