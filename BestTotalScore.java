import java.util.HashMap;
import java.util.TreeSet;

public class BestTotalScore implements Strategy {
    @Override
    public Student findBestStudent(TreeSet<Grade> grades) {
        return grades.last().getStudent();
    }
}
