import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class GraderPage extends JFrame {
    private JList<Student> studList;
    private JTextField selectedStudent;
    private JTextField partialScore;
    private JTextField examScore;
    private JTextField totalScore;
    private JTextField bestStudent;
    private JButton reset;
    private JButton submit;
    private JButton publish;
    private JButton saveSnapshot;
    private JButton logOut;
    private Student currentStudent;
    public GraderPage(Account account) {
        super("Welcome " + account.getUserName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 450));
        setLocation(new Point(600, 400));

        currentStudent = null;

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel tab1 = new JPanel();
        tab1.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        JLabel tabOneTitle = new JLabel("Welcome " + account.getUserName());
        tabOneTitle.setFont(new Font("Poppins", Font.BOLD, 16));
        tab1.add(tabOneTitle, constraints);

        StringTokenizer nameTokenizer = new StringTokenizer(account.getUserName());
        String teacherFirstName = nameTokenizer.nextToken();
        String teacherLastName = nameTokenizer.nextToken();
        Teacher teacher = new Teacher(teacherFirstName, teacherLastName);
        Assistant assistant = new Assistant(teacherFirstName, teacherLastName);
        Vector<Student> courseStudents = null;

        Course teacherCourse = null;
        if(account.getUserType().equals("Teacher")) {
            for(Course course : Catalog.getInstance().getCourses()) {
                if(course.getTeacher().equals(teacher)) {
                    teacherCourse = course;
                    break;
                }
            }
            courseStudents = new Vector<>(teacherCourse.getAllStudent());
        } else {
            courseStudents = new Vector<>();
            for(Course course : Catalog.getInstance().getCourses()) {
                if(course.getAssistants().contains(assistant)) {
                    teacherCourse = course;
                    for(Group group : course.getGroups().values()) {
                        if(group.getAssistant().equals(assistant)) {
                            for(Student student : group) {
                                courseStudents.add(student);
                            }
                        }
                    }
                    break;
                }
            }
        }


        studList = new JList<>(courseStudents);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 3;
        JScrollPane scrollableStudList = new JScrollPane(studList);
        scrollableStudList.setPreferredSize(new Dimension(200, 140));
        tab1.add(scrollableStudList, constraints);

        JPanel selStudPanel = new JPanel();
        selStudPanel.setBorder(new TitledBorder("Selected Student"));
        selStudPanel.setLayout(new FlowLayout());
        selectedStudent = new JTextField();
        selectedStudent.setPreferredSize(new Dimension(328, 20));
        selectedStudent.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        selStudPanel.add(selectedStudent, constraints);
        tab1.add(selStudPanel, constraints);

        JPanel gradesPanel = new JPanel();
        gradesPanel.setBorder(new TitledBorder("Student's grades"));
        gradesPanel.setLayout(new FlowLayout());

        JLabel psLabel = new JLabel("Partial: ");
        psLabel.setFont(new Font("Poppins", Font.BOLD, 11));
        partialScore = new JTextField();
        partialScore.setPreferredSize(new Dimension(60, 20));

        JLabel esLabel = new JLabel("Exam: ");
        esLabel.setFont(new Font("Poppins", Font.BOLD, 11));
        examScore = new JTextField();
        examScore.setPreferredSize(new Dimension(70, 20));
        if(account.getUserType().equals("Assistant"))
            examScore.setEditable(false);

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

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        tab1.add(gradesPanel, constraints);

        submit = new JButton("Save");
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        tab1.add(submit, constraints);

        Course finalTeacherCourse = teacherCourse;
        ListSelectionListener lsl = e -> {
            JList<Student> list = (JList<Student>) e.getSource();

            Student student = list.getSelectedValue();
            currentStudent = student;
            selectedStudent.setText(student.toString());

            Grade grade = finalTeacherCourse.getGrade(student);
            partialScore.setText(grade.getPartialScore().toString());
            examScore.setText(grade.getExamScore().toString());
            totalScore.setText(grade.getTotal().toString());

            ArrayList<Student> graduated = finalTeacherCourse.getGraduatedStudents();
            if(graduated.contains(student)) {
                if(finalTeacherCourse instanceof PartialCourse) {
                    partialScore.setBackground(new Color(95, 214, 71));
                } else {
                    partialScore.setBackground(new Color(95, 214, 71));
                    examScore.setBackground(new Color(95, 214, 71));
                }
            } else {
                if(finalTeacherCourse instanceof PartialCourse) {
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
        };

        studList.addListSelectionListener(lsl);

        ActionListener al = e -> {
            Grade grade = finalTeacherCourse.getGrade(currentStudent);
            Grade gradeClone = (Grade) grade.clone();
            gradeClone.setPartialScore(Double.parseDouble(partialScore.getText()));
            gradeClone.setExamScore(Double.parseDouble(examScore.getText()));
            finalTeacherCourse.getGrades().remove(grade);
            finalTeacherCourse.getGrades().add(gradeClone);
            totalScore.setText(gradeClone.getTotal().toString());
        };

        submit.addActionListener(al);

        JPanel tab2 = new JPanel();
        tab2.setLayout(new GridBagLayout());

        String bestStudentOptions[] = { "", "Best Partial Score", "Best Exam Score", "Best Total Score" };
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setBorder(new TitledBorder("Who's The Best?"));
        comboBoxPanel.setLayout(new FlowLayout());
        JComboBox<String> comboBox = new JComboBox<>(bestStudentOptions);
        JLabel bestStudentLabel = new JLabel("It's: ");
        bestStudentLabel.setFont(new Font("Poppins", Font.BOLD, 11));
        bestStudent = new JTextField();
        bestStudent.setPreferredSize(new Dimension(328, 20));
        comboBoxPanel.add(comboBox);
        comboBoxPanel.add(bestStudentLabel);
        comboBoxPanel.add(bestStudent);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        tab2.add(comboBoxPanel, constraints);

        ActionListener bestStudentListener = e -> {
            if(comboBox.getSelectedItem().equals("Best Partial Score")) {
                finalTeacherCourse.setStrategy(new BestPartialScore());
            } else if(comboBox.getSelectedItem().equals("Best Exam Score")) {
                finalTeacherCourse.setStrategy(new BestExamScore());
            } else if(comboBox.getSelectedItem().equals("Best Total Score")){
                finalTeacherCourse.setStrategy(new BestTotalScore());
            } else {
                finalTeacherCourse.setStrategy(null);
            }

            if(finalTeacherCourse.getStrategy() != null) {
                Student searchedStudent = finalTeacherCourse.getBestStudent();
                bestStudent.setText(searchedStudent.toString());
            } else {
                bestStudent.setText("");
            }
        };

        comboBox.addActionListener(bestStudentListener);

        publish = new JButton("Publish");
        JPanel publishPanel = new JPanel();
        publishPanel.setBorder(new TitledBorder("Upload Grades To Database"));
        publishPanel.add(publish);
        constraints.gridx = 0;
        constraints.gridy = 1;
        tab2.add(publishPanel, constraints);

        ActionListener publishButtonListener = e -> {
            try {
                overwriteGradesDatabase();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            ScoreVisitor visitor = new ScoreVisitor();
            teacher.accept(visitor);

        };

        publish.addActionListener(publishButtonListener);

        logOut = new JButton("LogOut");
        JPanel logOutPanel = new JPanel();
        logOutPanel.setBorder(new TitledBorder("LogOut"));
        logOutPanel.add(logOut);
        constraints.gridx = 0;
        constraints.gridy = 3;
        tab2.add(logOutPanel, constraints);

        ActionListener logOutButtonListener = e -> {
            try {
                App.activeWindow = new AuthenticationPage();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        };

        logOut.addActionListener(logOutButtonListener);

        saveSnapshot = new JButton("Save Snapshot");
        reset = new JButton("Restore");
        JPanel snapPanel = new JPanel();
        snapPanel.setBorder(new TitledBorder("Backup"));
        snapPanel.add(saveSnapshot);
        snapPanel.add(reset);
        constraints.gridx = 0;
        constraints.gridy = 2;

        ActionListener snapListener = e -> {
            finalTeacherCourse.makeBackup();
        };

        ActionListener restoreListener = e -> {
            finalTeacherCourse.undo();
        };

        saveSnapshot.addActionListener(snapListener);
        reset.addActionListener(restoreListener);

        tab2.add(snapPanel, constraints);

        tabbedPane.addTab("Grade Students", tab1);
        tabbedPane.addTab("Functions", tab2);
        add(tabbedPane);

        pack();
        setVisible(true);
    }

    public void overwriteGradesDatabase() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("D:\\School Stuff\\UPB ACS\\Anul II - 2022\\Tema\\TemaPOO\\src\\grades.txt");
        writer.println(Catalog.getInstance().getCourses().size());
        for(Course course : Catalog.getInstance().getCourses()) {
            writer.println(course.getName());
            writer.println(course.getAllStudent().size());
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
