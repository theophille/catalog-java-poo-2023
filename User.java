public abstract class User implements Comparable {
    private String firstName, lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(Object o) {
        int result = this.toString().compareTo(((User)o).toString());

        if(result < 0) {
            return -1;
        } else if(result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        User compStudent = (User) obj;
        if(compStudent.toString().equals(this.toString()))
            return true;
        return false;
    }
}
