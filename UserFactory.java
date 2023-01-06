public class UserFactory {
    public User getUser(String userType, String firstName, String lastName) {
        if(userType == "Student") {
            return new Student(firstName, lastName);
        } else if (userType == "Parent") {
            return new Parent(firstName, lastName);
        } else if (userType == "Assistant") {
            return new Assistant(firstName, lastName);
        } else if (userType == "Teacher") {
            return new Teacher(firstName, lastName);
        } else {
            return null;
        }
    }
}
