import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;

import static config.DatabaseConfig.*;
import static config.DatafileConfig.FILE_PATH;

public class DogShelter {
    /*
1, Hozzunk létre dog_shelter adatbazázist.
2, Csináljunk egy metódust ami megcsinálja a dogs táblánkat. ✔
3, Csináljunk egy metódust ami elmenti a data.txt -ből az adatainkat a táblázatba.  ✔
4, Csináljunk egy metódust ami paraméterként bekér egy booleant és azokat a kutyákat húzza fel az adatbázisbol ami a friendly oszlop megegyezik.  ✔
5, Csináljunk egy metódust ami ID alapján felhúz egy kutya adatot. ✔
6, Csináljunk egy metódust ami felhúzza az összes kutyát. ✔
7, Csináljunk egy metódust ami ID alapján tudja módostani a kutya életkorát ✔

EXTRA:
Csináljunk egy metódust ami kér 2 számot paraméterként. Az egyik a limit lesz a másik az offset. Kérjünk ki mindig annyi kutyát ami a limit
és hagyjunk ki annyi kutyát ami az offset ✔
     */

    public static void main(String[] args) {
//        createTable();
//        readDogs();
//        System.out.println("Friendly dogs:");
//        selectFriendlyDogs(true);
//        System.out.println("Unfriendly dogs:");
//        selectFriendlyDogs(false);
//        selectDogById(3);
//        selectAllDogs();
//        updateAge(2, 7);
        pageDogs(3, 6);
    }

    private static void pageDogs(int offset, int limit) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String selectDog =  "SELECT id, name FROM dogs LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectDog);
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(String.join(" ", resultSet.getString("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateAge(int id, int newAge) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String updateDog =  "UPDATE dogs SET age = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateDog);
            preparedStatement.setInt(1, newAge);
            preparedStatement.setInt(2, id);
            int updatedRows = preparedStatement.executeUpdate();
            System.out.println("Updated " + updatedRows + " rows");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectAllDogs() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String selectDog =  "SELECT * FROM dogs";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectDog);
            while (resultSet.next()) {
                System.out.println(String.join(" ", resultSet.getString("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectDogById(int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String selectDog =  "SELECT * FROM dogs WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectDog);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(String.join(" ", resultSet.getString("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectFriendlyDogs(boolean isFriendly) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String selectDog =  "SELECT * FROM dogs WHERE friendly = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectDog);
            preparedStatement.setBoolean(1, isFriendly);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(String.join(" ", resultSet.getString("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String createTable =  "CREATE TABLE IF NOT EXISTS dogs (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "breed VARCHAR(50)," +
                    "name VARCHAR(50)," +
                    "age INT," +
                    "friendly BOOLEAN" +
                    ")";
            PreparedStatement preparedStatement = connection.prepareStatement(createTable);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readDogs() {
        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                String breed = fields[0];
                String name = fields[1];
                int age = Integer.parseInt(fields[2]);
                boolean friendly = Boolean.parseBoolean(fields[3]);
                insertIntoDatabase(breed, name, age, friendly);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // miért nem true false van táblában
    private static void insertIntoDatabase(String breed, String name, int age, boolean friendly) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String insertDog =  "INSERT INTO dogs" +
                    "(breed, name, age, friendly)" +
                    "VALUES" +
                    "(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertDog);
            preparedStatement.setString(1, breed);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.setBoolean(4, friendly);
            System.out.println("Inserted " + preparedStatement.executeUpdate() + " rows");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
