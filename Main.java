import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    
        // try ini sebenernya bisa di hapus, alesan pake try biar scanner otomatis ditutup biar gak memory leak
        try (Scanner sc = new Scanner(System.in)) { 
            HashMap<String, BankUser> db = new HashMap<>();
            SessionStack session = new SessionStack();
            TransferService transferService = new TransferService();

            // DATA PUNYA USER
            db.put("UMN123", new BankUser("UMN123", "123456", 1000000));

            boolean appRunning = true;
            while (appRunning) {
                System.out.println("\n=== WELCOME TO MOBILE BCA (UMN) ===");
                System.out.print("Username: ");
                String user = sc.next();

                if (!db.containsKey(user)) {
                    System.out.println("User Not Found.");
                    continue; 
                }

                BankUser current = db.get(user);

                if (current.isLocked()) {
                    System.out.println("Account Locked. Silakan hubungi Customer Service.");
                    break; 
                }

                System.out.print("PIN: ");
                String inputPin = sc.next();

                if (current.validatePin(inputPin)) {
                    session.login(user);
                    System.out.println("Login Successfully! Selamat Datang " + session.getCurrentUser());

                    boolean sessionActive = true;
                    while (sessionActive) {
                        current.displayMenu(); 
                        System.out.print("Pilih Menu: ");
                        int menu = sc.nextInt();

                        switch (menu) {
                            case 1: // SHOW BALANCE
                                System.out.println("\nSaldo Anda: Rp " + current.getBalance());
                                break;
                            case 2: // TRANSFER
                                handleTransfer(sc, current, transferService);
                                break;
                            case 3: // VIRTUAL ACCOUNT
                                handleVirtualAccount(sc, current, transferService);
                                break;
                            case 4: // QRIS
                                handleQRIS(sc, current, transferService);
                                break;
                            case 5: // LOGOUT
                                session.logout();
                                sessionActive = false;
                                System.out.println("Logout Success. Session Cleared.");
                                break;
                            default:
                                System.out.println("Menu tidak valid.");
                        }
                    }
                } else {
                    current.addFailedAttempt();
                    System.out.println("PIN Salah!"); 
                }
            }
        }
    } 

   // IF ELSE UNTUK PILIHAN MENU

    private static void handleTransfer(Scanner sc, BankUser current, TransferService ts) {
        System.out.println("\n=== MENU TRANSFER ===");
        System.out.println("1. Antar-Bank\n2. Antar-Rekening\n3. Back");
        System.out.print("Pilih Tipe: ");
        int tipe = sc.nextInt();
        if (tipe == 3) return;

        String bank = "BCA";
        if (tipe == 1) {
            System.out.println("\nPilih Bank Tujuan:");
            System.out.println("1. BNI\n2. BRI\n3. Mandiri");
            System.out.print("Pilih: ");
            int pilBank = sc.nextInt();
            bank = (pilBank == 1) ? "BNI" : (pilBank == 2) ? "BRI" : "Mandiri";
        }

        System.out.print("Masukkan Nomor Rekening: ");
        String rek = sc.next();
        
        System.out.print("Masukkan Nominal Transfer: ");
        double nom = sc.nextDouble();

        if (tipe == 1) {
            System.out.println("\nPilih Layanan Transfer:");
            System.out.println("1. BI-FAST (Rp 2.500)\n2. Realtime Online (Rp 6.500)");
            System.out.print("Pilih: ");
            sc.nextInt();
        }

        System.out.println("\n--- KONFIRMASI TRANSFER ---");
        System.out.println("Bank Tujuan : " + bank);
        System.out.println("No. Rekening: " + rek);
        System.out.println("Total       : Rp " + nom);
        System.out.print("Apakah data sudah sesuai? (y/n): ");
        
        if (sc.next().equalsIgnoreCase("y")) {
            System.out.print("Masukkan PIN Anda: ");
            if (current.validatePin(sc.next())) {
                ts.process(current, nom, rek);
            } else {
                System.out.println("PIN Salah. Transaksi otomatis dibatalkan.");
            }
        }
    }

    private static void handleVirtualAccount(Scanner sc, BankUser current, TransferService ts) {
        System.out.println("\n=== VIRTUAL ACCOUNT ===");
        System.out.println("1. Top up E-Wallet\n2. Utility Bills\n3. Back");
        System.out.print("Pilih: ");
        int tipe = sc.nextInt();
        if (tipe == 3) return;

        if (tipe == 1) {
            System.out.println("\nPilih Provider: 1. GOPAY, 2. ShopeePay, 3. Dana");
            System.out.print("Pilih: ");
            sc.nextInt();
            System.out.print("Masukkan Nomor HP: ");
            String hp = sc.next();
            System.out.print("Masukkan Nominal: ");
            double nom = sc.nextDouble();

            System.out.print("Konfirmasi (y/n)? ");
            if (sc.next().equalsIgnoreCase("y")) {
                System.out.print("Masukkan PIN: ");
                if (current.validatePin(sc.next())) ts.process(current, nom, hp);
            }
        } else {
            System.out.println("\nPilih Tagihan: 1. Listrik, 2. Air");
            System.out.print("Pilih: ");
            sc.nextInt();
            System.out.print("Masukkan No Pelanggan: ");
            String no = sc.next();
            System.out.println("\n--- DETAIL TAGIHAN ---\nNama: Mahasiswa UMN\nTotal: Rp 150.000");
            System.out.print("Konfirmasi? (y/n): ");
            if (sc.next().equalsIgnoreCase("y")) {
                System.out.print("PIN: ");
                if (current.validatePin(sc.next())) ts.process(current, 150000, no);
            }
        }
    }

    private static void handleQRIS(Scanner sc, BankUser current, TransferService ts) {
        System.out.println("\n=== QRIS PAYMENT ===");
        System.out.print("Scan ID Merchant: ");
        String m = sc.next();
        System.out.print("Masukkan Nominal: ");
        double n = sc.nextDouble();
        System.out.print("Konfirmasi PIN: ");
        if (current.validatePin(sc.next())) {
            ts.process(current, n, m);
            System.out.println("Berhasil! Sisa Saldo: Rp " + current.getBalance());
        }
    }
} 


