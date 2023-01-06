import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ScoreVisitor implements Visitor {
    private HashMap<Teacher, Tuple<Student, String, Double>> examScores;
    private HashMap<Assistant, Tuple<Student, String, Double>> partialScores;

    ScoreVisitor() {
        examScores = new HashMap<Teacher, Tuple<Student, String, Double>>();
        partialScores = new HashMap<Assistant, Tuple<Student, String, Double>>();
    }

    @Override
    public void visit(Assistant assistant) {
        ArrayList<Course> courses = Catalog.getInstance().getCourses();
        Group desiredGroup = null;

        for(Course course : courses) {
            TreeSet<Assistant> courseAssistants = course.getAssistants();

            if(courseAssistants.contains(assistant)) {
                HashMap<String, Group> groups = course.getGroups();

                for(Map.Entry<String, Group> entry : groups.entrySet()) {
                    Group group = entry.getValue();
                    if(group.getAssistant().toString().equals(assistant.toString())) {
                        desiredGroup = group;
                        break;
                    }
                }

                for(Grade grade : course.getGrades()) {
                    if(desiredGroup.contains(grade.getStudent())) {
                        Tuple<Student, String, Double> gradeData = new Tuple<Student, String, Double>(grade.getStudent(), grade.getCourse(), grade.getPartialScore());
                        partialScores.put(assistant, gradeData);
                        Catalog.getInstance().notifyObservers(grade);
                    }
                }
            }
        }
    }

    @Override
    public void visit(Teacher teacher) {
        ArrayList<Course> courses = Catalog.getInstance().getCourses();

        for(Course course : courses) {
            if(course.getTeacher().toString().equals(teacher.toString())) {
                for(Grade grade : course.getGrades()) {
                    Tuple<Student, String, Double> gradeData = new Tuple<Student, String, Double>(grade.getStudent(), grade.getCourse(), grade.getExamScore());
                    examScores.put(teacher, gradeData);
                    Catalog.getInstance().notifyObservers(grade);
                }
            }
        }
    }

    private class Tuple<K extends Student, L extends String, M extends Double> {
        private K student;
        private L course;
        private M grade;

        Tuple(K student, L course, M grade) {
            this.student = student;
            this.course = course;
            this.grade = grade;
        }

        public K getStudent() {
            return student;
        }

        public L getCourse() {
            return course;
        }

        public M getGrade() {
            return grade;
        }
    };
}
