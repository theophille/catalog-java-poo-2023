import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Group extends TreeSet<Student> {
    private Assistant assistant;
    private String ID;

    public Group(String ID, Assistant assistant) {
        super();
        this.ID = ID;
        this.assistant = assistant;
    }

    public Group(String ID, Assistant assistant, Comparator<Student> comp) {
        super(comp);
        this.ID = ID;
        this.assistant = assistant;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public String getID() {
        return ID;
    }

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
