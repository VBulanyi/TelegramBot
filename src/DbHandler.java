import org.sqlite.JDBC;

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

    private Connection connection;

    private DbHandler() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    public List<Subscribtion> getAllSubscribtions() {


        try (Statement statement = this.connection.createStatement()) {

            List<Subscribtion> subscribtions = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery("SELECT id , message FROM Subscribtions");

            while (resultSet.next()) {
                subscribtions.add(new Subscribtion(resultSet.getString("id"),
                        resultSet.getString("message")));
            }

            return subscribtions;
        } catch (SQLException e) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
            return Collections.emptyList();
        }
   }

    // Добавление подписки в БД
    public void addSubscribtion(Subscribtion subscribtion) {
        // Создадим подготовленное выражение, чтобы избежать SQL-инъекций
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO Subscribtions(id, message) " +
                        "VALUES(?,?)")) {
            statement.setObject(1, subscribtion.id);
            statement.setObject(2, subscribtion.message);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteSubscribtion(String id) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM Subscribtions WHERE id = ?")) {
            statement.setObject(1, id);
            // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


