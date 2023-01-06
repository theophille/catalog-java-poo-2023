import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UsersDatabase {
    private ArrayList<Account> accounts;

    public UsersDatabase(File file) throws FileNotFoundException {
        accounts = new ArrayList<>();
        create(file);
    }

    private void create(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()) {
            String userType = scanner.nextLine();
            String userName = scanner.nextLine();
            String password = scanner.nextLine();

            try {
                scanner.nextLine();
            } catch (NoSuchElementException e) {
                continue;
            }

            Account account = new Account(userType, userName, password);
            accounts.add(account);
        }
    }

    public void print() {
        for(Account account : accounts) {
            System.out.println(account.getUserType());
            System.out.println(account.getUserName());
            System.out.println(account.getPassword());
            System.out.println();
        }
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
