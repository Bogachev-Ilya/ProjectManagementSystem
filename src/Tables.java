import java.sql.*;

public class Tables {

    private ResultSet resultSet;

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void createTablesAndInit(String URL, String tableName) {

        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("DROP TABLE IF EXISTS " + tableName + ";");

            statement.executeUpdate();
            PreparedStatement statement1 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName +
                    "(Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, Project TEXT, Task TEXT, Responsible TEXT, Phone_Email TEXT, StartDate TEXT, Task_Duration TEXT, IS_TaskComplite INTEGER);");
            statement1.executeUpdate();
            PreparedStatement statement2 = connection.prepareStatement("INSERT INTO " + tableName + " (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('NotStandardJava', 'Create new Lib', 'Bob', '456-789-00, bob@java.net', '2017-01-01', '2 years', 0);");
            PreparedStatement statement3 = connection.prepareStatement("INSERT INTO " + tableName + " (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('NotStandardJava', 'Update Standard Classes', 'Paul', '456-789-01, paul@java.net', '2017-06-20', '6 month', 1);");
            PreparedStatement statement4 = connection.prepareStatement("INSERT INTO " + tableName + " (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('Browser FirsFast', 'GUI', 'Jak', '456-789-12, jak@java.net', '2016-03-17', '2 month', 1);");
            PreparedStatement statement5 = connection.prepareStatement("INSERT INTO " + tableName + " (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('Android App', 'Network', 'Piter', '456-789-99, piter@java.net', '2018-03-19', '5 month', 0);");
            PreparedStatement statement6 = connection.prepareStatement("INSERT INTO " + tableName + " (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('Android App', 'Visual Style', 'Julia', '456-789-74, julia@java.net', '2017-11-03', '7 month', 0);");
            PreparedStatement statement7 = connection.prepareStatement("INSERT INTO " + tableName + " (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('Android App', 'App logic', 'Troy', '456-000-01, troy@java.net', '2017-02-10', '1 year', 0);");
            PreparedStatement statement8 = connection.prepareStatement("INSERT INTO " + tableName + " (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('Android App', 'Transaction', 'Jak', '456-789-12, jak@java.net', '2017-05-20', '5 month', 1);");

            statement2.executeUpdate();
            statement3.executeUpdate();
            statement4.executeUpdate();
            statement5.executeUpdate();
            statement6.executeUpdate();
            statement7.executeUpdate();
            statement8.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void showProjectInWork(String URL, String tableName) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT Project FROM " + tableName + " WHERE IS_TaskComplite = 0;");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("Project");
                System.out.printf("Projects in work: %s\n", name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incompleteTasks(String URL, String tableName, String projectName) {
        try (Connection connection = DriverManager.getConnection(URL)) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT (Task) FROM " + tableName + " WHERE IS_TaskComplite = 0 AND Project= ?;");
            statement.setString(1, projectName);
            resultSet = statement.executeQuery();
            int count = resultSet.getInt(1);
            System.out.printf("Incomplete tasks for %s is: %d\n", projectName, count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
