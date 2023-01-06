import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class WindowFactory {
    public JFrame getWindow(String userType, Account account) throws FileNotFoundException {
        if(userType.equals("Auth")) {
            return new AuthenticationPage();
        } else if(userType.equals("Student")) {
            return new StudentPage(account);
        } else if(userType.equals("Parent")){
            return new ParentPage(account);
        } else {
            return new GraderPage(account);
        }
    }
}
