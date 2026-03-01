interface TransactionProcessor {
    void process(BankUser user, double amount, String target);
}

public class TransferService implements TransactionProcessor {
    @Override
    public void process(BankUser user, double amount, String target) {
        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            System.out.println("Transfer ke " + target + " Berhasil!");
        } else {
            System.out.println("Saldo Tidak Cukup!");
        }
    }
}
