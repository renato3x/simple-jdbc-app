package application;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;

public class Main {
  public static void main(String[] args) {
    Connection connection = null;
    String query = "";
    String[][] usersToSave = { { "Renato", "renato@gmail.com" }, { "Jorge", "jorge@gmail.com" }, { "Rita", "rita@gmail.com" } };
    PreparedStatement statement = null;

    try (FileInputStream propertiesFile = new FileInputStream("db.properties")) {

      //preparing database connection
      Properties dbProps = new Properties();
      dbProps.load(propertiesFile);

      connection = DriverManager.getConnection(dbProps.getProperty("dburl"), dbProps);

      // inserting users in database
      query = "INSERT INTO users (username, email) VALUES (?, ?)";
      for (String[] user : usersToSave) {
        statement = connection.prepareStatement(query);

        statement.setString(1, user[0]);
        statement.setString(2, user[1]);

        statement.executeUpdate();
      }

      // receiving and showing users
      System.out.println("Users:");
      ResultSet users = connection.createStatement().executeQuery("SELECT * FROM users");

      while (users.next()) {
        int id = users.getInt("id");
        String username = users.getString("username");
        String email = users.getString("email");

        System.out.println("id: " + id + ", " + "username: " + username + ", email: " + email + "\n");
      }

      // updating user
      query = "UPDATE users SET username = ? WHERE id = ?";
      statement = connection.prepareStatement(query);
      statement.setString(1, "Moana");
      statement.setInt(2, new Random().nextInt(3));
      int rowsAffected = statement.executeUpdate();

      System.out.println("Update made. " + rowsAffected + " rows affected\n");

      //deleting user
      query = "DELETE FROM users WHERE id = ?";
      statement = connection.prepareStatement(query);
      statement.setInt(1, new Random().nextInt(3));

      rowsAffected = statement.executeUpdate();
      System.out.println("Delete made. " + rowsAffected + " rows affected\n");

      // receiving and showing users again
      System.out.println("Users:");
      users = connection.createStatement().executeQuery("SELECT * FROM users");

      while (users.next()) {
        int id = users.getInt("id");
        String username = users.getString("username");
        String email = users.getString("email");

        System.out.println("id: " + id + ", " + "username: " + username + ", email: " + email + "\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
