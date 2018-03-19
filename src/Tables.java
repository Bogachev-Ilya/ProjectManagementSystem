import java.sql.*;

public class Tables {

    public void createTablesAndInit(String URL){

        try (Connection connection = DriverManager.getConnection(URL)){
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS ProjectManagment" +
                    "(Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, Project TEXT, Task TEXT, Responsible TEXT, Phone_Email TEXT, StartDate TEXT, Task_Duration TEXT, IS_TaskComplite INTEGER);");
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
