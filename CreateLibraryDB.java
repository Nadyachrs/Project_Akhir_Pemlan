// Nama     :NADYA CHARRISA GABRIELA
// NIM      :235150400111009
// Kelas    :SI-A

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateLibraryDB {
    static final String DB_URL = "jdbc:mysql://localhost:3306/";
    static final String USER = "root";
    static final String PASS = "Nad147";

    public static void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement stmt = conn.createStatement();

            // Create database if it does not exist
            String sql = "CREATE DATABASE IF NOT EXISTS LibraryDB";
            stmt.executeUpdate(sql);

            // Use the created database
            String useDbSQL = "USE LibraryDB";
            stmt.executeUpdate(useDbSQL);

            // Drop tables if they already exist
            String dropPeminjamTableSQL = "DROP TABLE IF EXISTS peminjam_buku";
            stmt.executeUpdate(dropPeminjamTableSQL);

            String dropBooksTableSQL = "DROP TABLE IF EXISTS books";
            stmt.executeUpdate(dropBooksTableSQL);

            String dropUsersTableSQL = "DROP TABLE IF EXISTS users";
            stmt.executeUpdate(dropUsersTableSQL);

            // Create books table
            String createBooksTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                                         "title VARCHAR(100)," +
                                         "author VARCHAR(100)," +
                                         "quantity INT)";
            stmt.executeUpdate(createBooksTableSQL);

            // Create users table
            String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                                         "id INT AUTO_INCREMENT PRIMARY KEY," +
                                         "username VARCHAR(100) NOT NULL," +
                                         "password VARCHAR(100) NOT NULL)";
            stmt.executeUpdate(createUsersTableSQL);

            // Insert default user
            String insertDefaultUserSQL = "INSERT INTO users (username, password) VALUES ('Nacapaex', 'admin123')";
            stmt.executeUpdate(insertDefaultUserSQL);

            // Reset AUTO_INCREMENT to 1
            String resetBooksIdSQL = "ALTER TABLE books AUTO_INCREMENT = 1";
            stmt.executeUpdate(resetBooksIdSQL);

            // Insert initial data into books table
            String insertBooksDataSQL = "INSERT INTO books (title, author, quantity) VALUES " +
                                        "('The Great Gatsby', 'F. Scott Fitzgerald', 5)," +
                                        "('To Kill a Mockingbird', 'Harper Lee', 3)," +
                                        "('1984', 'George Orwell', 4)," +
                                        "('Pride and Prejudice', 'Jane Austen', 6)," +
                                        "('The Catcher in the Rye', 'J.D. Salinger', 2)," +
                                        "('The Alchemist', 'Paulo Coelho', 7)," +
                                        "('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 9)," +
                                        "('The Lord of the Rings', 'J.R.R. Tolkien', 8)," +
                                        "('Animal Farm', 'George Orwell', 6)," +
                                        "('Jane Eyre', 'Charlotte Bronte', 4)," +
                                        "('Moby-Dialog', 'Herman Melville', 5)," +
                                        "('The Picture of Dorian Gray', 'Oscar Wilde', 3)," +
                                        "('Brave New World', 'Aldous Huxley', 6)," +
                                        "('Gone with the Wind', 'Margaret Mitchell', 4)," +
                                        "('The Hobbit', 'J.R.R. Tolkien', 7)," +
                                        "('The Da Vinci Code', 'Dan Brown', 8)," +
                                        "('Anna Karenina', 'Leo Tolstoy', 5)," +
                                        "('The Odyssey', 'Homer', 6)," +
                                        "('Wuthering Heights', 'Emily Bronte', 4)," +
                                        "('One Hundred Years of Solitude', 'Gabriel Garcia Marquez', 7)";
            stmt.executeUpdate(insertBooksDataSQL);

            // Create peminjam_buku table
            String createPeminjamTableSQL = "CREATE TABLE IF NOT EXISTS peminjam_buku (" +
                                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                                            "book_id INT," +
                                            "nama_peminjam VARCHAR(100)," +
                                            "nomor_telepon VARCHAR(20)," +
                                            "jumlah_pinjam INT," +
                                            "FOREIGN KEY (book_id) REFERENCES books(id))";
            stmt.executeUpdate(createPeminjamTableSQL);

            System.out.println("Database and tables created successfully with initial data.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setupDatabase();
    }
}
