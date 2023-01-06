import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        initializeCatalog();
        testCatalog();
    }

    public static void testCatalog() {
        Catalog catalog = Catalog.getInstance();
        ArrayList<Course> courses = catalog.getCourses();

        for(Course course : courses) {
            System.out.println("Course --> " + course.getName() + " {");
            System.out.println("\tTeacher: " + course.getTeacher());
            System.out.println();
            System.out.println("\tAssistants: {");

            for (Assistant assistant : course.getAssistants()) {
                System.out.println("\t\t" + assistant);
            }

            System.out.println("\t}");
            System.out.println();

            for(Grade grade : course.getGrades()) {
                System.out.println(grade);
            }

            for (Map.Entry<String, Group> entry : course.getGroups().entrySet()) {
                System.out.println("\tGroup " + entry.getKey() + " {");
                for(Student student : entry.getValue()) {
                    System.out.println("\t\t" + student + " {");
                    System.out.println("\t\t\tMother: " + student.getMother());
                    System.out.println("\t\t\tFather: " + student.getFather());

                    Grade currentStudentGrade = course.getGrade(student);
                    System.out.println("\t\t\tPartialScore: " + currentStudentGrade.getPartialScore());

                    if(course instanceof FullCourse) {
                        System.out.println("\t\t\tExamScore: " + currentStudentGrade.getExamScore());
                        System.out.println("\t\t\tTotalScore: " + currentStudentGrade.getTotal());
                    }

                    System.out.println("\t\t}");
                    System.out.println();
                }
                System.out.println("\t}");
                System.out.println();
            }
            System.out.println("}\n");
        }

//        for(Course course : courses) {
//            System.out.println();
//            System.out.println("Course: " + course.getName());
//            System.out.println("Best student:");
//            Student best = course.getBestStudent();
//            if(course instanceof FullCourse) {
//                System.out.println("Exam: " + best + " ---> " + course.getGrade(best).getExamScore());
//                course.setStrategy(new BestTotalScore());
//                System.out.println("Total: " + best + " ---> " + course.getGrade(best).getTotal());
//            } else {
//                System.out.println("Partial: " + best + " ---> " + course.getGrade(best).getPartialScore());
//            }
//        }

//        System.out.println();
//        System.out.println("The teachers have just uploaded all the grades");
//        System.out.println();
//
        ScoreVisitor visitor = new ScoreVisitor();

        for (Course course : courses) {
            Teacher teacher = course.getTeacher();
            System.out.println(teacher);
            teacher.accept(visitor);
            System.out.println();
        }
//
//        for (Course course : courses) {
//            TreeSet<Assistant> assistants = course.getAssistants();
//            for(Assistant assistant : assistants) {
//                System.out.println(assistant);
//                assistant.accept(visitor);
//                System.out.println();
//            }
//        }
//
//        System.out.println();
//        System.out.println();
//        ArrayList<Student> students = courses.get(0).getAllStudent();
//        for(Student student : students) {
//            System.out.println(student.getFather());
//            System.out.println(student.getMother());
//        }
    }

    public static void initializeCatalog() throws FileNotFoundException{
        Catalog catalog = Catalog.getInstance();
        ArrayList<Course> courses = Catalog.getInstance().getCourses();
        File data = new File("D:\\School Stuff\\UPB ACS\\Anul II - 2022\\Tema\\TemaPOO\\src\\courses.txt");
        Scanner scanner = new Scanner(data);
        int coursesNumber = Integer.parseInt(scanner.nextLine());
        UserFactory factory = new UserFactory();

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

                    Grade grade = new Grade();
                    grade.setPartialScore(0.0);
                    if(courseType.equals("Full")) {
                        grade.setExamScore(0.0);
                    }
                    grade.setCourse(courseName);
                    grade.setStudent(student);
                    course.addGrade(grade);
                }
                course.addGroup(group);
            }

            courses.add(course);
        }
    }
}