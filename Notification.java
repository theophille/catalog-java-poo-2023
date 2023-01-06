public class Notification {
    private Parent parent;
    private Student student;
    private Grade grade;

    public Notification(Parent parent, Student student, Grade grade) {
        this.parent = parent;
        this.student = student;
        this.grade = grade;
    }

    public Parent getParent() {
        return parent;
    }

    public Grade getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Mr/s " + parent + ", " + student + "'s new grade is " + grade.getTotal();
    }

    @Override
    public boolean equals(Object obj) {
        Notification compNotif = (Notification) obj;
        if(grade.getTotal().equals(compNotif.grade.getTotal())) {
            return true;
        }
        return false;
    }
}
