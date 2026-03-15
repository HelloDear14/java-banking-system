import javax.swing.*;
import java.awt.*;

public class NIETBankGUI {

    NIETBankBackend bank = new NIETBankBackend();

    public void showMainMenu() {
        JFrame menu = new JFrame("NIET Bank");
        menu.setSize(300, 250);
        menu.setLayout(new GridLayout(4, 1));

        JLabel title = new JLabel("💠 Welcome to NIET BANK 💠", SwingConstants.CENTER);
        JButton createBtn = new JButton("Create Account");
        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");

        menu.add(title);
        menu.add(createBtn);
        menu.add(loginBtn);
        menu.add(exitBtn);

        // Create account window
        createBtn.addActionListener(e -> showCreateAccount());

        // Login window
        loginBtn.addActionListener(e -> showLogin());

        exitBtn.addActionListener(e -> System.exit(0));

        menu.setVisible(true);
    }

    // Create Account GUI
    void showCreateAccount() {
        JFrame f = new JFrame("Create Account");
        f.setSize(400, 400);
        f.setLayout(new GridLayout(8, 1));

        JTextField nameF = new JTextField();
        JTextField aadhaarF = new JTextField();
        JPasswordField passF = new JPasswordField();
        JTextField balF = new JTextField();
        JButton createBtn = new JButton("Create Account");

        f.add(new JLabel("Enter Name:"));
        f.add(nameF);
        f.add(new JLabel("Enter Aadhaar:"));
        f.add(aadhaarF);
        f.add(new JLabel("Create Password:"));
        f.add(passF);
        f.add(new JLabel("Opening Balance:"));
        f.add(balF);
        f.add(createBtn);

        createBtn.addActionListener(e -> {
            try {
                bank.createAccountDirect(
                        nameF.getText(),
                        aadhaarF.getText(),
                        new String(passF.getPassword()),
                        Double.parseDouble(balF.getText())
                );

                JOptionPane.showMessageDialog(f, "Account Created Successfully!");
                f.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error: " + ex.getMessage());
            }
        });

        f.setVisible(true);
    }

    // Login window
    void showLogin() {
        JFrame f = new JFrame("Login");
        f.setSize(300, 200);
        f.setLayout(new GridLayout(4, 1));

        JTextField aadhaarF = new JTextField();
        JPasswordField passF = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        f.add(new JLabel("Enter Aadhaar:"));
        f.add(aadhaarF);
        f.add(new JLabel("Enter Password:"));
        f.add(passF);
        f.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String accNo = bank.loginDirect(aadhaarF.getText(), new String(passF.getPassword()));
            if (accNo != null) {
                JOptionPane.showMessageDialog(f, "Login Successful!");
                f.dispose();
                showDashboard(accNo);
            } else {
                JOptionPane.showMessageDialog(f, "Invalid Credentials!");
            }
        });

        f.setVisible(true);
    }

    // Dashboard
    void showDashboard(String accNo) {
        JFrame f = new JFrame("Dashboard");
        f.setSize(400, 400);
        f.setLayout(new GridLayout(6, 1));

        JButton balBtn = new JButton("Show Balance");
        JButton depBtn = new JButton("Deposit");
        JButton witBtn = new JButton("Withdraw");
        JButton histBtn = new JButton("Transaction History");
        JButton logoutBtn = new JButton("Logout");

        f.add(balBtn);
        f.add(depBtn);
        f.add(witBtn);
        f.add(histBtn);
        f.add(logoutBtn);

        balBtn.addActionListener(e -> {
            double bal = bank.getBalance(accNo);
            JOptionPane.showMessageDialog(f, "Balance: ₹" + bal);
        });

        depBtn.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog("Amount to deposit:");
            bank.depositDirect(accNo, Double.parseDouble(amt));
        });

        witBtn.addActionListener(e -> {
            String amt = JOptionPane.showInputDialog("Amount to withdraw:");
            bank.withdrawDirect(accNo, Double.parseDouble(amt));
        });

        histBtn.addActionListener(e -> {
            String history = bank.getHistory(accNo);
            JTextArea ta = new JTextArea(history);
            JOptionPane.showMessageDialog(f, new JScrollPane(ta));
        });

        logoutBtn.addActionListener(e -> f.dispose());

        f.setVisible(true);
    }
}
