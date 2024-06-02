// Nama     :NADYA CHARRISA GABRIELA
// NIM      :235150400111009
// Kelas    :SI-A

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Username : Nacapaex
// Password : admin123

public class Login extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/LibraryDB";
    static final String USER = "root";
    static final String PASS = "Nad147";

    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelLogin = new JPanel(new GridLayout(3, 2));

        JLabel labelUsername = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel labelPassword = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton tombolLogin = new JButton("Login");
        tombolLogin.addActionListener(e -> login());

        panelLogin.add(labelUsername);
        panelLogin.add(usernameField);
        panelLogin.add(labelPassword);
        panelLogin.add(passwordField);
        panelLogin.add(new JLabel());
        panelLogin.add(tombolLogin);

        add(panelLogin, BorderLayout.CENTER);
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login berhasil!");
                new AplikasiReservasiPerpustakaan();
                this.dispose(); // Menutup form login
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}

