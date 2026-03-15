import java.sql.*;

public class NIETBankBackend {

    Connection conn = DBConnection.getConnection();

    //  CREATE ACCOUNT
    public void createAccountDirect(String name, String aadhaar, String pass, double bal) throws SQLException {
        String accNo = "SHV" + (int) (Math.random() * 10000 + 1000);
        double minBal = 1000;

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users (aadhaar, name, password, acc_no, balance, min_balance) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, aadhaar);
        ps.setString(2, name);
        ps.setString(3, pass);
        ps.setString(4, accNo);
        ps.setDouble(5, bal);
        ps.setDouble(6, minBal);
        ps.executeUpdate();
    }

    // LOGIN
    public String loginDirect(String aadhaar, String pass) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE aadhaar=? AND password=?");
            ps.setString(1, aadhaar);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("acc_no");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    // GET BALANCE
    public double getBalance(String accNo) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT balance FROM users WHERE acc_no=?");
            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    //DEPOSIT
    public void depositDirect(String accNo, double amt) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET balance = balance + ? WHERE acc_no=?");
            ps.setDouble(1, amt);
            ps.setString(2, accNo);
            ps.executeUpdate();
            saveTransaction(accNo, "Deposit", amt);
        } catch (Exception ignored) {}
    }

    //WITHDRAW
    public void withdrawDirect(String accNo, double amt) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT balance, min_balance FROM users WHERE acc_no=?");
            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double bal = rs.getDouble("balance");
                double minBal = rs.getDouble("min_balance");

                if (bal - amt < minBal) return;

                PreparedStatement up = conn.prepareStatement("UPDATE users SET balance = balance - ? WHERE acc_no=?");
                up.setDouble(1, amt);
                up.setString(2, accNo);
                up.executeUpdate();

                saveTransaction(accNo, "Withdraw", amt);
            }
        } catch (Exception ignored) {}
    }

    //HISTORY
    public String getHistory(String accNo) {
        StringBuilder sb = new StringBuilder();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactions WHERE acc_no=?");
            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                sb.append(rs.getTimestamp("date"))
                        .append(" | ")
                        .append(rs.getString("type"))
                        .append(" ₹")
                        .append(rs.getDouble("amount"))
                        .append("\n");
            }
        } catch (Exception ignored) {}

        return sb.toString();
    }

    //SAVE TRANSACTION
    void saveTransaction(String accNo, String type, double amt) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transactions (acc_no, type, amount) VALUES (?, ?, ?)");
        ps.setString(1, accNo);
        ps.setString(2, type);
        ps.setDouble(3, amt);
        ps.executeUpdate();
    }
}
