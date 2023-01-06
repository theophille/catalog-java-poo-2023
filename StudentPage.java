import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.*;

public class StudentPage extends JFrame {
    private JList<Course> coursesList;
    private JTextField teacher;
    private JTextField studAssistant;
    private JTextField partialScore;
    private JTextField examScore;
    private JTextField totalScore;
    private JTextArea assistants;
    private Vector<Course> studCourses;

    public StudentPage(Account account) {
        super("Welcome " + account.getUserName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 450));
        setLocation(new Point(600, 400));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        Catalog catalog = Catalog.getInstance();

        JLabel pageTitle = new JLabel("Welcome " + account.getUserName());
        pageTitle.setFont(new Font("Poppins", Font.BOLD, 16));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        add(pageTitle, constraints);

        StringTokenizer nameTokenizer = new StringTokenizer(account.getUserName());
        String studentFirstName = nameTokenizer.nextToken();
        String studentLastName = nameTokenizer.nextToken();
        Student student = new Student(studentFirstName, studentLastName);

        studCourses = getStudentCourses(student);
        coursesList = new JList<>(studCourses);
        coursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(coursesList);
        scrollPane.setBorder(new TitledBorder("Select course"));
        scrollPane.setPreferredSize(new Dimension(400, 100));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(scrollPane, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        JLabel teacherLabel = new JLabel("Teacher: ");
        teacherLabel.setFont(new Font("Poppins", Font.BOLD, 12));
        add(teacherLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        teacher = new JTextField();
        teacher.setEditable(false);
        teacher.setPreferredSize(new Dimension(300, 25));
        add(teacher, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel assistantsLabel = new JLabel("Assistants: ");
        assistantsLabel.setFont(new Font("Poppins", Font.BOLD, 12));
        add(assistantsLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        assistants = new JTextArea();
        assistants.setPreferredSize(new Dimension(300, 70));
        assistants.setEditable(false);
        assistants.setOpaque(false);
        JScrollPane assistScrollPane = new JScrollPane(assistants);
        add(assistScrollPane, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        JLabel studAssistLabel = new JLabel("Your Assistant: ");
        studAssistLabel.setFont(new Font("Poppins", Font.BOLD, 12));
        add(studAssistLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        studAssistant = new JTextField();
        studAssistant.setEditable(false);
        studAssistant.setPreferredSize(new Dimension(300, 25));
        add(studAssistant, constraints);

        JPanel gradesPanel = new JPanel();
        gradesPanel.setBorder(new TitledBorder("Your grades"));
        gradesPanel.setLayout(new FlowLayout());

        JLabel psLabel = new JLabel("Partial: ");
        psLabel.setFont(new Font("Poppins", Font.BOLD, 11));
        partialScore = new JTextField();
        partialScore.setEditable(false);
        partialScore.setPreferredSize(new Dimension(60, 20));

        JLabel esLabel = new JLabel("Exam: ");
        esLabel.setFont(new Font("Poppins", Font.BOLD, 11));
        examScore = new JTextField();
        examScore.setEditable(false);
        examScore.setPreferredSize(new Dimension(70, 20));

        JLabel tsLabel = new JLabel("Total: ");
        tsLabel.setFont(new Font("Poppins", Font.BOLD, 11));
        totalScore = new JTextField();
        totalScore.setEditable(false);
        totalScore.setPreferredSize(new Dimension(60, 20));

        gradesPanel.add(psLabel);
        gradesPanel.add(partialScore);
        gradesPanel.add(esLabel);
        gradesPanel.add(examScore);
        gradesPanel.add(tsLabel);
        gradesPanel.add(totalScore);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        add(gradesPanel, constraints);

        ListSelectionListener lsl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList<Course> list = (JList<Course>) e.getSource();

                Course selectedCourse = list.getSelectedValue();
                teacher.setText(selectedCourse.getTeacher().toString());

                String output = "";
                for(Assistant assistant : selectedCourse.getAssistants()) {
                    output += assistant.toString() + "\n";
                }
                assistants.setText(output);

                HashMap<String, Group> groups = selectedCourse.getGroups();
                for(Map.Entry<String, Group> entry : groups.entrySet()) {
                    Group group = entry.getValue();
                    if(group.contains(student)) {
                        studAssistant.setText(group.getAssistant().toString());
                        break;
                    }
                }

                Grade grade = selectedCourse.getGrade(student);
                partialScore.setText(grade.getPartialScore().toString());
                examScore.setText(grade.getExamScore().toString());
                totalScore.setText(grade.getTotal().toString());
                ArrayList<Student> graduated = selectedCourse.getGraduatedStudents();
                if(graduated.contains(student)) {
                    if(selectedCourse instanceof PartialCourse) {
                        partialScore.setBackground(new Color(95, 214, 71));
                    } else {
                        partialScore.setBackground(new Color(95, 214, 71));
                        examScore.setBackground(new Color(95, 214, 71));
                    }
                } else {
                    if(selectedCourse instanceof PartialCourse) {
                        partialScore.setBackground(new Color(222, 60, 60));
                    } else {
                        partialScore.setBackground(new Color(222, 60, 60));
                        examScore.setBackground(new Color(222, 60, 60));
                    }
                }

                if(grade.totalScoreCheck()) {
                    totalScore.setBackground(new Color(95, 214, 71));
                } else {
                    totalScore.setBackground(new Color(222, 60, 60));
                }
            }
        };

        coursesList.addListSelectionListener(lsl);

        pack();
        setVisible(true);
    }

    private Vector<Course> getStudentCourses(Student student) {
        Vector<Course> courses = new Vector<>();
        for(Course course : Catalog.getInstance().getCourses()) {
            if(course.getAllStudent().contains(student))
                courses.add(course);
        }
        return courses;
    }
}
