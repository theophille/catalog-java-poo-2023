import java.util.*;

public abstract class Course {
    private String name;
    private Teacher teacher;
    private TreeSet<Assistant> assistants;
    private TreeSet<Grade> grades;
    private HashMap<String, Group> groups;
    private int creditPoints;
    private Strategy strategy;
    private Snapshot snapshot;

    public Course(CourseBuilder builder) {
        this.name = builder.name;
        this.teacher = builder.teacher;
        this.assistants = builder.assistants;
        this.grades = builder.grades;
        this.groups = builder.groups;
        this.creditPoints = builder.creditPoints;
        this.strategy = builder.strategy;
        this.snapshot = builder.snapshot;
    }

    // setters and getters

    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public TreeSet<Assistant> getAssistants() {
        return assistants;
    }

    public TreeSet<Grade> getGrades() {
        return grades;
    }

    public HashMap<String, Group> getGroups() {
        return groups;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setAssistants(TreeSet<Assistant> assistants) {
        this.assistants = assistants;
    }

    public void setGrades(TreeSet<Grade> grades) {
        this.grades = grades;
    }

    public void setGroups(HashMap<String, Group> groups) {
        this.groups = groups;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    // demanded methods

    public void addAssistant(String ID, Assistant assistant) {
        if(!assistants.contains(assistant))
            assistants.add(assistant);

        Group group = groups.get(ID);
        if(group != null) {
            group.setAssistant(assistant);
        }
    }

    public void addStudent(String ID, Student student) {
        Group group = groups.get(ID);
        if(group != null) {
            group.add(student);
        } else {
            System.err.println("Group not found. The student could not be added.");
        }
    }

    public void addGroup(Group group) {
        groups.put(group.getID(), group);
    }

    public void addGroup(String ID, Assistant assistant) {
        Group newGroup = new Group(ID, assistant);
        groups.put(ID, newGroup);
    }

    public void addGroup(String ID, Assistant assistant, Comparator<Student> comp) {
        Group newGroup = new Group(ID, assistant, comp);
        groups.put(ID, newGroup);
    }

    public Grade getGrade(Student student) {
        for (Grade grade:grades) {
            if(grade.getStudent().toString().equals(student.toString()))
                return grade;
        }
        return null;
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public ArrayList<Student> getAllStudent() {
        ArrayList<Student> students = new ArrayList<Student>();

        for(Map.Entry<String, Group> entry : groups.entrySet()) {
            Group currGroup = entry.getValue();
            for(Student s : currGroup)
                students.add(s);
        }

        return students;
    }

    public HashMap<Student, Grade> getAllStudentGrades() {
        HashMap<Student, Grade> studentGrades = new HashMap<Student, Grade>();

        for (Grade grade : grades)
            studentGrades.put(grade.getStudent(), grade);

        return studentGrades;
    }

    public Student getBestStudent() {
        return strategy.findBestStudent(grades);
    }

    public void makeBackup() {
        snapshot = new Snapshot();
        snapshot.save();
    }

    public void undo() {
        if(snapshot != null)
            snapshot.restore();
    }

    public abstract ArrayList<Student> getGraduatedStudents();

    static abstract class CourseBuilder {
        private String name;
        private Teacher teacher;
        private TreeSet<Assistant> assistants;
        private TreeSet<Grade> grades;
        private HashMap<String, Group> groups;
        private int creditPoints;
        private Strategy strategy;
        private Snapshot snapshot;

        public CourseBuilder(String name, Teacher teacher) {
            this.name = name;
            this.teacher = teacher;
            this.assistants = new TreeSet<Assistant>();

            Comparator<Grade> comparator = (o1, o2) -> {
                int compResult = o1.compareTo(o2);
                if(compResult < 0) {
                    return -1;
                } else if(compResult > 0) {
                    return 1;
                } else {
                    Grade g1 = (Grade) o1;
                    Grade g2 = (Grade) o2;
                    int nameCompResult = g1.getStudent().toString().compareTo(g2.getStudent().toString());
                    if(nameCompResult < 0) {
                        return -1;
                    } else if(nameCompResult > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };

            this.grades = new TreeSet<Grade>(comparator);
            this.groups = new HashMap<String, Group>();
            this.creditPoints = 0;
            this.strategy = null;
            this.snapshot = null;
        }

        public abstract Course build();
    };

    private class Snapshot {
        private TreeSet<Grade> grades;

        public Snapshot() {
            Comparator<Grade> comparator = (o1, o2) -> {
                int compResult = o1.compareTo(o2);
                if(compResult < 0) {
                    return -1;
                } else if(compResult > 0) {
                    return 1;
                } else {
                    Grade g1 = (Grade) o1;
                    Grade g2 = (Grade) o2;
                    int nameCompResult = g1.getStudent().toString().compareTo(g2.getStudent().toString());
                    if(nameCompResult < 0) {
                        return -1;
                    } else if(nameCompResult > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };

            grades = new TreeSet<Grade>(comparator);
        }

        public void save() {
            for(Grade grade : Course.this.grades) {
                Grade clonedGrade = (Grade) grade.clone();
                grades.add(clonedGrade);
            }
        }

        public void restore() {
            Course.this.grades = grades;
        }
    };

    @Override
    public String toString() {
        return name;
    }
}
