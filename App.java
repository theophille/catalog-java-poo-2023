import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class App {
    public static JFrame activeWindow;

    public static void main(String[] args) throws FileNotFoundException {
        initializeCatalog();
        Account account = new Account("Parent", "Florin Popescu", "Gp9FEZ");
        activeWindow = new AuthenticationPage();
    }

    public static void initializeCatalog() throws FileNotFoundException{
        Catalog catalog = Catalog.getInstance();
        ArrayList<Course> courses = Catalog.getInstance().getCourses();
        File data = new File("D:\\School Stuff\\UPB ACS\\Anul II - 2022\\Tema\\TemaPOO\\src\\courses.txt");
        Scanner scanner = new Scanner(data);
        int coursesNumber = Integer.parseInt(scanner.nextLine());
        UserFactory factory = new UserFactory();
        GradesDatabase gdb = new GradesDatabase(new File("D:\\School Stuff\\UPB ACS\\Anul II - 2022\\Tema\\TemaPOO\\src\\grades.txt"));

        for(int i = 0; i < coursesNumber; i++) {
            String courseType = scanner.nextLine();
            String courseName = scanner.nextLine();
            String teacherName = scanner.nextLine();
            StringTokenizer tokenizer = new StringTokenizer(teacherName, " ");
            String teacherFirstName = tokenizer.nextToken();
            String teacherLastName = tokenizer.nextToken();
            Teacher teacher = (Teacher) factory.getUser("Teacher", teacherFirstName, teacherLastName);

            Course course;
            if(courseType.equals("Full")) {
                FullCourse.FullCourseBuilder builder = new FullCourse.FullCourseBuilder(courseName, teacher);
                course = builder.build();
            } else {
                PartialCourse.PartialCourseBuilder builder = new PartialCourse.PartialCourseBuilder(courseName, teacher);
                course = builder.build();
            }

            int groupsNumber = Integer.parseInt(scanner.nextLine());
            Comparator<Student> comp = (o1, o2) -> {
                String name1 = o1.toString();
                String name2 = o2.toString();
                int compResult = name1.compareTo(name2);

                if(compResult < 0) {
                    return -1;
                } else if(compResult > 0) {
                    return 1;
                } else {
                    return 0;
                }
            };

            for(int j = 0; j < groupsNumber; j++) {
                String groupID = scanner.nextLine();
                String assistantName = scanner.nextLine();
                StringTokenizer assistantTokenizer = new StringTokenizer(assistantName, " ");
                String assistantFirstName = assistantTokenizer.nextToken();
                String assistantLastName = assistantTokenizer.nextToken();
                Assistant assistant = (Assistant) factory.getUser("Assistant", assistantFirstName, assistantLastName);
                course.addAssistant(groupID, assistant);
                int studentsNumber = Integer.parseInt(scanner.nextLine());
                Group group = new Group(groupID, assistant, comp);

                for(int k = 0; k < studentsNumber; k++) {
                    String studentName = scanner.nextLine();
                    StringTokenizer studTokenizer = new StringTokenizer(studentName, " ");
                    String studentFirstName = studTokenizer.nextToken();
                    String studentLastName = studTokenizer.nextToken();
                    Student student = (Student) factory.getUser("Student", studentFirstName, studentLastName);

                    String motherName = scanner.nextLine();
                    StringTokenizer motherTokenizer = new StringTokenizer(motherName, " ");
                    String motherFirstName = motherTokenizer.nextToken();
                    String motherLastName = motherTokenizer.nextToken();
                    Parent mother = (Parent) factory.getUser("Parent", motherFirstName, motherLastName);

                    String fatherName = scanner.nextLine();
                    StringTokenizer fatherTokenizer = new StringTokenizer(fatherName, " ");
                    String fatherFirstName = fatherTokenizer.nextToken();
                    String fatherLastName = fatherTokenizer.nextToken();
                    Parent father = (Parent) factory.getUser("Parent", fatherFirstName, fatherLastName);

                    student.setMother(mother);
                    student.setFather(father);
                    group.add(student);

                    Grade grade = gdb.getGrade(student, course.getName());
                    course.addGrade(grade);
                }
                course.addGroup(group);
            }

            courses.add(course);
        }
    }

    public static void initializeGradesDatabase() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File("D:\\School Stuff\\UPB ACS\\Anul II - 2022\\Tema\\TemaPOO\\src\\grades.txt"));
        for(Course course : Catalog.getInstance().getCourses()) {
            writer.println(course.getName());
            for(Student student : course.getAllStudent()) {
                writer.println(student);
                Grade grade = course.getGrade(student);
                writer.println(grade.getPartialScore());
                writer.println(grade.getExamScore());
            }
        }
        writer.close();
    }
}
