import java.util.HashMap;
import java.util.TreeSet;

public interface Strategy {
    Student findBestStudent(TreeSet<Grade> grades);
}
