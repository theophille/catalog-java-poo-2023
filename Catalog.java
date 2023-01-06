import java.util.ArrayList;

public class Catalog implements Subject {
    private ArrayList<Course> courses;
    private ArrayList<Observer> observers;
    private static Catalog catalog;

    private Catalog() {
        catalog = null;
        courses = new ArrayList<Course>();
        observers = new ArrayList<Observer>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public static Catalog getInstance() {
        if(catalog == null)
            catalog = new Catalog();
        return catalog;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Grade grade) {
        boolean x = false;
        for (Observer obs : observers) {
            obs.update(new Notification(grade.getStudent().getFather(), grade.getStudent(), grade));
            obs.update(new Notification(grade.getStudent().getMother(), grade.getStudent(), grade));
        }
    }
}
