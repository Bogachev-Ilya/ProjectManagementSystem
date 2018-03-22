import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class TablesTest {
    Tables tables;

    @Before
    public void setUp() throws Exception {


        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tables = new Tables();
        tables.createTablesAndInit("jdbc:sqlite:test.db", "ProjectManagment");
    }


    @Test
    public void createTablesAndInit() throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Project FROM ProjectManagment WHERE Task = 'GUI';");
            while (resultSet.next()) {
                String name = resultSet.getString("Project");
                assertEquals("Browser FirsFast", name);
            }
        }
    }

    @Test
    public void testShowProjectInWork() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS PM");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS PM" +
                    "(Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, Project TEXT, Task TEXT, Responsible TEXT, Phone_Email TEXT, StartDate TEXT, Task_Duration TEXT, IS_TaskComplite INTEGER);");

            statement.executeUpdate("INSERT INTO PM (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('NotStandardJava', 'Create new Lib', 'Bob', '456-789-00, bob@java.net', '2017-01-01', '2 years', 0);");
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT Project FROM PM WHERE IS_TaskComplite = 0;");
            String name = resultSet.getString("Project");
            tables.showProjectInWork("jdbc:sqlite:test.db", "PM");
            assertEquals(name, tables.getResultSet().getString("Project"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCountIncompleteProjectTasks() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS IncompleteTasks");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS IncompleteTasks" +
                    "(Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, Project TEXT, Task TEXT, Responsible TEXT, Phone_Email TEXT, StartDate TEXT, Task_Duration TEXT, IS_TaskComplite INTEGER);");

            statement.executeUpdate("INSERT INTO IncompleteTasks (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                    "VALUES ('NotStandardJava', 'Create new Lib', 'Bob', '456-789-00, bob@java.net', '2017-01-01', '1 year', 0);");
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (Task) FROM IncompleteTasks WHERE (IS_TaskComplite =0 AND Project='NotStandardJava');");
            int count = resultSet.getInt(1);
            tables.countIncompleteProjectTasks("jdbc:sqlite:test.db", "IncompleteTasks" , "NotStandardJava");
            assertEquals(count, tables.getResultSet().getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testIncompleteResponsibleTasks(){
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Task FROM IncompleteTasks WHERE (Responsible='Bob' AND IS_TaskComplite =0);");
            String tasks =resultSet.getString("Task");
            tables.incompleteResponsibleTasks("jdbc:sqlite:test.db", "IncompleteTasks", "Bob");
            assertEquals(tasks, tables.getResultSet().getString("Task"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}