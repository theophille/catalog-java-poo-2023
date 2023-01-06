public class Account {
    private String userType;
    private String userName;
    private String password;

    public Account(String userType, String userName, String password) {
        this.userType = userType;
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public boolean equals(Object obj) {
        Account comp = (Account) obj;
        if(userName.equals(comp.userName) && password.equals(comp.password))
            return true;
        return false;
    }
}
