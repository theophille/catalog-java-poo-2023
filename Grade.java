public class Grade implements Comparable, Cloneable {
    private Double partialScore, examScore;
    private Student student;
    private String course;

    public Grade() {
        partialScore = 0.0;
        examScore = 0.0;
        student = null;
        course = "";
    }

    public Grade(Double partialScore, Double examScore, Student student, String course) {
        this.partialScore = partialScore;
        this.examScore = examScore;
        this.student = student;
        this.course = course;
    }

    public Double getPartialScore() {
        return partialScore;
    }

    public Double getExamScore() {
        return examScore;
    }

    public Student getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }

    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Double getTotal() {
        return partialScore + examScore;
    }

    public boolean totalScoreCheck() {
        Double total = getTotal();
        if(total.compareTo(5.0) >= 0)
            return true;
        return false;
    }

    @Override
    public int compareTo(Object o) {
        Grade compGrade = (Grade)o;
        Double thisGrade = getTotal();
        Double compareToGrade = compGrade.getTotal();

        if(thisGrade < compareToGrade) {
            return -1;
        } else if(thisGrade > compareToGrade) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Object clone() {
        Grade clonedGrade = new Grade();
        Double clonedPartialScore = partialScore.doubleValue();
        Double clonedExamScore = examScore.doubleValue();
        clonedGrade.setExamScore(clonedExamScore);
        clonedGrade.setPartialScore(clonedPartialScore);
        clonedGrade.setStudent(student);
        clonedGrade.setCourse(course);
        return clonedGrade;
    }

    @Override
    public String toString() {
        return student + ": " + getTotal().toString();
    }
}
