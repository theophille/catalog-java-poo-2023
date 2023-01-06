import java.util.HashMap;
import java.util.TreeSet;

public class BestExamScore implements Strategy {
    @Override
    public Student findBestStudent(TreeSet<Grade> grades) {
        Grade maxGrade = grades.first();

        for(Grade grade : grades) {
            if(maxGrade.getExamScore() < grade.getExamScore()) {
                maxGrade = grade;
            }
        }

        return maxGrade.getStudent();
    }
}
