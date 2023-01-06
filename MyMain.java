import java.io.File;
import java.io.FileNotFoundException;

public class MyMain {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\School Stuff\\UPB ACS\\Anul II - 2022\\Tema\\TemaPOO\\src\\users.txt");
        UsersDatabase usersDatabase = new UsersDatabase(file);
        usersDatabase.print();
    }
}
