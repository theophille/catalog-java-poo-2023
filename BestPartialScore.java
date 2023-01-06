import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class BestPartialScore implements Strategy {
    @Override
    public Student findBestStudent(TreeSet<Grade> grades) {
        Grade maxGrade = grades.first();

        for(Grade grade : grades) {
            if(maxGrade.getPartialScore() < grade.getPartialScore()) {
                maxGrade = grade;
            }
        }

        return maxGrade.getStudent();
    }
}
