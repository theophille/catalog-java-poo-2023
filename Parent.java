import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parent extends User implements Observer {
    private ArrayList<Notification> notifications;

    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
        if(!observerAlreadyExists())
            Catalog.getInstance().addObserver(this);
        notifications = new ArrayList<>();
    }

    private boolean observerAlreadyExists() {
        ArrayList<Observer> observers = Catalog.getInstance().getObservers();
        for(Observer obs : observers) {
            Parent p = (Parent) obs;
            if(p.toString().equals(this.toString()))
                return true;
        }
        return false;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    @Override
    public void update(Notification notification) {
        if(this.toString().equals(notification.getParent().toString())) {
            if(!notifications.contains(notification)) {
                notifications.add(notification);
                System.out.println(this + ": " + notifications.size());
            }
        }
    }
}
