package config;

public class DatabaseConfig {
    public static final String DB_URL =
            "jdbc:mysql://localhost:3306/dog_shelter?useSSL=false&serverTimezone=UTC&allowMultiQueries=true&createDatabaseIfNotExist=true";
    public static final String USER = "root";
    public static final String PASSWORD = "Test123!";
}
