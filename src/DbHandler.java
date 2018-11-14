import org.sqlite.JDBC;
import sun.plugin2.message.Message;

import javax.xml.stream.Location;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class DbHandler {

    private static final String CON_STR = "jdbc:sqlite:C://Users//vbulaniy//IdeaProjects//TelegramBot//src//subscribtion.db";

    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null)
            instance = new DbHandler();
        return instance;

    }
// Подключение к БД

    private Connection connection;

    public DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    // Получение данных из БД

    public HashMap<Long, String> getAllSubscribtions() {


        try (Statement statement = this.connection.createStatement()) {

            HashMap<Long, String> subscribtions = new HashMap<>();

            ResultSet resultSet = statement.executeQuery("SELECT chatId , location FROM Subscribtions");

            while (resultSet.next()) {
                subscribtions.put(
                        resultSet.getLong("chatId"),
                        resultSet.getString("location"));
//                        resultSet.getString("idCity")));
            }

            return subscribtions;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
            }
        }


    // Добавление подписки в БД
    public void addSubscribtion(Subscribtion subscribtion) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO Subscribtions(chatId, location) " +
                        "VALUES(?,?)")) {
            statement.setObject(1, subscribtion.chatId);
            statement.setObject(2, subscribtion.location);
//            statement.setObject(3, subscribtion.idCity);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteSubscribtion(String chatId, Location location) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM Subscribtions WHERE chatId = ?, location = ?")) {
            statement.setObject(1, chatId);
            statement.setObject(2, location);
//            statement.setObject(3, idCity);

            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
