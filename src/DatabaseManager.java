import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/customs";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    public static void addDriver(String name, String licenseNumber) {
        String query = "INSERT INTO drivers (full_name, license_number) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, licenseNumber);
            pstmt.executeUpdate();
            System.out.println("✅ Водитель " + name + " добавлен в базу данных.");
        } catch (SQLException e) {
            System.out.println("❌ Ошибка при добавлении водителя: " + e.getMessage());
        }
    }

    public static void getAllDrivers() {
        String query = "SELECT * FROM drivers";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("📋 Список водителей пуст.");
                return;
            }

            System.out.println("📋 Список водителей:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("full_name");
                String licenseNumber = rs.getString("license_number");
                System.out.println("ID: " + id + ", Имя: " + name + ", Вод. удостоверение: " + licenseNumber);
            }
        } catch (SQLException e) {
            System.out.println("❌ Ошибка при получении списка водителей: " + e.getMessage());
        }
    }

    public static void deleteDriver(int id) {
        String query = "DELETE FROM drivers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("✅ Водитель с ID " + id + " удален.");
            } else {
                System.out.println("⚠ Водитель с ID " + id + " не найден.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Ошибка при удалении водителя: " + e.getMessage());
        }
    }

    public static User findUserByUsername(String username) {
        String query = "SELECT u.id, u.username, u.password_hash, r.role_name " +
                "FROM users u JOIN roles r ON u.role_id = r.id " +
                "WHERE u.username = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role_name")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Ошибка при поиске пользователя: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        getAllDrivers();
    }
}