import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GradesDatabase {
    private ArrayList<Grade> grades;

    public GradesDatabase(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int courses = Integer.parseInt(scanner.nextLine());
        grades = new ArrayList<>();

        for(int i = 0; i < courses; i++) {
            String courseName = scanner.nextLine();
            int students = Integer.parseInt(scanner.nextLine());
            for(int j = 0; j < students; j++) {
                String studentName = scanner.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(studentName);
                String firstName = tokenizer.nextToken();
                String lastName = tokenizer.nextToken();

                Double partialScore = Double.parseDouble(scanner.nextLine());
                Double examScore = Double.parseDouble(scanner.nextLine());

                Grade grade = new Grade(partialScore, examScore, new Student(firstName, lastName), courseName);
                grades.add(grade);
            }
        }
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public Grade getGrade(Student student, String course) {
        for (Grade grade:grades) {
            if(grade.getStudent().toString().equals(student.toString()) && grade.getCourse().equals(course)) {
                grade.setStudent(student);
                return grade;
            }
        }
        return null;
    }
}
