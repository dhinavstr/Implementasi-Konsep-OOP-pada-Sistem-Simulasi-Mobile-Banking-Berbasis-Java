public class BankUser extends Account {
    private int failedAttempts = 0;
    private boolean isLocked = false;

    public BankUser(String username, String pin, double balance) {
        super(username, pin, balance);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- [HOME] ---");
        System.out.println("1. Check Balance\n2. Transfer\n3. Virtual Account\n4. QRIS Payment\n5. Logout");
    }

    public void addFailedAttempt() {
        this.failedAttempts++;
        if (this.failedAttempts >= 3) this.isLocked = true;
    }

    public boolean isLocked() { return isLocked; }
}
