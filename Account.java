public abstract class Account {
    protected String username;
    private final String pin; 
    protected double balance;

    public Account(String username, String pin, double balance) {
        this.username = username;
        this.pin = pin;
        this.balance = balance;
    }

    public double getBalance() { return balance; }
    
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void setBalance(double balance) { this.balance = balance; }
    
    public abstract void displayMenu();
}
