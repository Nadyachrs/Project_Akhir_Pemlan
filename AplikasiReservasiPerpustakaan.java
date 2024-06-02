// Nama     :NADYA CHARRISA GABRIELA
// NIM      :235150400111009
// Kelas    :SI-A

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class AplikasiReservasiPerpustakaan extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/LibraryDB";
    static final String USER = "root";
    static final String PASS = "Nad147";

    private JPanel panelUtama;
    private JTable tabelBuku;
    private DefaultTableModel modelTabel;

    public AplikasiReservasiPerpustakaan() {
        setTitle("Sistem Reservasi Perpustakaan");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(new Color(255, 255, 153)); // Warna kuning muda

        // Panel Pesan Selamat Datang
        JPanel panelSelamatDatang = new JPanel();
        panelSelamatDatang.setBackground(new Color(255, 255, 153));
        JLabel labelSelamatDatang = new JLabel("RESERVASI BUKU PERPUSTAKAAN TAMBERNAD");
        labelSelamatDatang.setFont(new Font("Times New Roman", Font.BOLD, 24));
        panelSelamatDatang.add(labelSelamatDatang);

        // Panel Tombol
        JPanel panelTombol = new JPanel(new FlowLayout());
        panelTombol.setBackground(new Color(255, 255, 153));
        JButton tombolTambah = new JButton("Tambah Buku");
        tombolTambah.addActionListener(e -> tambahBuku());
        JButton tombolReservasi = new JButton("Buat Reservasi");
        tombolReservasi.addActionListener(e -> buatReservasi());
        JButton tombolEdit = new JButton("Edit Jumlah Buku");
        tombolEdit.addActionListener(e -> editJumlahBuku());
        JButton tombolHapus = new JButton("Hapus Buku");
        tombolHapus.addActionListener(e -> hapusBuku());
        JButton tombolLihatPeminjam = new JButton("Lihat Daftar Peminjam");
        tombolLihatPeminjam.addActionListener(e -> lihatDaftarPeminjam());
        JButton tombolLogout = new JButton("Logout");
        tombolLogout.addActionListener(e -> logout());

        panelTombol.add(tombolTambah);
        panelTombol.add(tombolReservasi);
        panelTombol.add(tombolEdit);
        panelTombol.add(tombolHapus);
        panelTombol.add(tombolLihatPeminjam);
        panelTombol.add(tombolLogout);

        // Tabel Buku
        String[] kolom = {"ID", "Title", "Author", "Quantity"};
        modelTabel = new DefaultTableModel(kolom, 0);
        tabelBuku = new JTable(modelTabel);
        Font fontTabel = new Font("Arial", Font.PLAIN, 18);
        tabelBuku.setFont(fontTabel);

        JTableHeader header = tabelBuku.getTableHeader();
        header.setBackground(new Color(255, 182, 193)); // Warna pink muda
        header.setForeground(Color.black);
        header.setFont(new Font("Arial", Font.BOLD, 20)); // Ukuran font untuk header tabel

        tabelBuku.setGridColor(new Color(255, 182, 193)); // Warna pink muda
        tabelBuku.setSelectionBackground(new Color(255, 105, 180)); // Warna pink tua
        tabelBuku.setSelectionForeground(Color.white);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelBuku.getColumnCount(); i++) {
            tabelBuku.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tabelBuku);
        scrollPane.getViewport().setBackground(new Color(255, 255, 153)); // Warna kuning muda

        panelUtama.add(panelSelamatDatang, BorderLayout.NORTH);
        panelUtama.add(panelTombol, BorderLayout.SOUTH);
        panelUtama.add(scrollPane, BorderLayout.CENTER);

        add(panelUtama);
        lihatBukuTersedia();
        setVisible(true);
    }

    private void tambahBuku() {
        String judul = JOptionPane.showInputDialog("Masukkan judul buku:");
        String penulis = JOptionPane.showInputDialog("Masukkan nama penulis:");
        int jumlah;
        try {
            jumlah = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jumlah buku:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
            return;
        }

        String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, judul);
            pstmt.setString(2, penulis);
            pstmt.setInt(3, jumlah);
            pstmt.executeUpdate();
            lihatBukuTersedia();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buatReservasi() {
        int selectedRow = tabelBuku.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku terlebih dahulu.");
            return;
        }
        int idBuku = (int) modelTabel.getValueAt(selectedRow, 0); // Ambil ID buku dari baris yang dipilih

        String namaPeminjam = JOptionPane.showInputDialog("Masukkan nama peminjam:");
        String nomorTelepon = JOptionPane.showInputDialog("Masukkan nomor telepon:");
        int jumlah;
        try {
            jumlah = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jumlah buku yang ingin dipinjam:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
            return;
        }
        String sqlCheck = "SELECT quantity FROM books WHERE id = ?";
        String sqlInsert = "INSERT INTO peminjam_buku (book_id, nama_peminjam, nomor_telepon, jumlah_pinjam) VALUES (?, ?, ?, ?)";
        String sqlUpdate = "UPDATE books SET quantity = quantity - ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {
            pstmtCheck.setInt(1, idBuku);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                if (quantity >= jumlah) {
                    pstmtInsert.setInt(1, idBuku);
                    pstmtInsert.setString(2, namaPeminjam);
                    pstmtInsert.setString(3, nomorTelepon);
                    pstmtInsert.setInt(4, jumlah);
                    pstmtInsert.executeUpdate();

                    pstmtUpdate.setInt(1, jumlah);
                    pstmtUpdate.setInt(2, idBuku);
                    pstmtUpdate.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Reservasi berhasil dibuat.");
                    lihatBukuTersedia(); // Perbarui tabel setelah membuat reservasi
                } else {
                    JOptionPane.showMessageDialog(this, "Jumlah buku yang tersedia tidak mencukupi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "ID buku tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void editJumlahBuku() {
        int selectedRow = tabelBuku.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku terlebih dahulu.");
            return;
        }
        int idBuku = (int) modelTabel.getValueAt(selectedRow, 0); // Ambil ID buku dari baris yang dipilih
    
        int jumlahBaru;
        try {
            jumlahBaru = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jumlah baru:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
            return;
        }
        String sql = "UPDATE books SET quantity = quantity + ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jumlahBaru);
            pstmt.setInt(2, idBuku);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Jumlah buku berhasil diperbarui.");
                lihatBukuTersedia(); // Perbarui tabel setelah mengedit jumlah buku
            } else {
                JOptionPane.showMessageDialog(this , "ID buku tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void hapusBuku() {
        int selectedRow = tabelBuku.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dihapus.");
            return;
        }
        int bookId = (int) modelTabel.getValueAt(selectedRow, 0);
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            lihatBukuTersedia();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void lihatDaftarPeminjam() {
        String sql = "SELECT pb.id, b.title, pb.nama_peminjam, pb.nomor_telepon, pb.jumlah_pinjam " +
                     "FROM peminjam_buku pb JOIN books b ON pb.book_id = b.id";
        DefaultTableModel modelPeminjam = new DefaultTableModel(new String[]{"ID", "Judul Buku", "Nama Peminjam", "Nomor Telepon", "Jumlah Pinjam"}, 0);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String judulBuku = rs.getString("title"); // Ambil judul buku
                String namaPeminjam = rs.getString("nama_peminjam");
                String nomorTelepon = rs.getString("nomor_telepon");
                int jumlahPinjam = rs.getInt("jumlah_pinjam");
                modelPeminjam.addRow(new Object[]{id, judulBuku, namaPeminjam, nomorTelepon, jumlahPinjam});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        JTable tabelPeminjam = new JTable(modelPeminjam);
        JScrollPane scrollPanePeminjam = new JScrollPane(tabelPeminjam);
        JTableHeader header = tabelPeminjam.getTableHeader();
        header.setBackground(new Color(255, 182, 193));
        Font fontHeader = new Font("Arial", Font.BOLD, 18);
        header.setFont(fontHeader); // Atur font header
        scrollPanePeminjam.getViewport().setBackground(new Color(255, 255, 153)); // Warna kuning muda
    
        // Atur perataan untuk kolom nomor ID
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabelPeminjam.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabelPeminjam.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Jumlah Pinjam

        JDialog dialogPeminjam = new JDialog(this, "Daftar Peminjam", true);
        dialogPeminjam.setSize(800, 400);
        dialogPeminjam.add(scrollPanePeminjam);
        dialogPeminjam.setVisible(true);
    }
    
    private void lihatBukuTersedia() {
        modelTabel.setRowCount(0);
        String sql = "SELECT * FROM books";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");
                Object[] row = {id, title, author, quantity};
                modelTabel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void logout(){
        JOptionPane.showMessageDialog(this, "Logout successful.");
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        new Login();
});
    
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         new AplikasiReservasiPerpustakaan();
    //     });
    }
}
