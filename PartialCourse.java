import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PartialCourse extends Course {
    public PartialCourse(CourseBuilder builder) {
        super(builder);
    }

    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<Student>();
        HashMap<Student, Grade> studentsGrades = getAllStudentGrades();

        for(Map.Entry<Student, Grade> entry : studentsGrades.entrySet()) {
            Grade grade = entry.getValue();
            if(grade.getPartialScore() >= 5.0)
                graduatedStudents.add(entry.getKey());
        }

        Comparator<Student> comp = (o1, o2) -> {
            String name1 = o1.getFirstName();
            String name2 = o2.getFirstName();
            int compResult = name1.compareTo(name2);

            if(compResult < 0) {
                return -1;
            } else if(compResult > 0) {
                return 1;
            } else {
                return 0;
            }
        };

        graduatedStudents.sort(comp);

        return graduatedStudents;
    }

    static class PartialCourseBuilder extends CourseBuilder {
        public PartialCourseBuilder(String name, Teacher teacher) {
            super(name, teacher);
        }

        @Override
        public Course build() {
            return new PartialCourse(this);
        }
    }
}
