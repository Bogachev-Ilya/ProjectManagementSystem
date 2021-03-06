import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class TablesTest {
    Tables tables;
    Connection connection;
    Statement statement;
    @Before
    public void setUp() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try  {
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        }catch (SQLException e){
            e.printStackTrace();
        }
        statement = connection.createStatement();
        tables = new Tables();
        tables.createTablesAndInit("jdbc:sqlite:test.db", "ProjectManagment");
        statement.executeUpdate("DROP TABLE IF EXISTS PM");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS PM" +
                "(Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, Project TEXT, Task TEXT, Responsible TEXT, Phone_Email TEXT, StartDate TEXT, Task_Duration INTEGER, IS_TaskComplite INTEGER);");

        statement.executeUpdate("INSERT INTO PM (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                "VALUES ('NotStandardJava', 'Create new Lib', 'Bob', '456-789-00, bob@java.net', '2017-01-01 00:00:00', 320, 0);");
        statement.executeUpdate("DROP TABLE IF EXISTS IncompleteTasks");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS IncompleteTasks" +
                "(Id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, Project TEXT, Task TEXT, Responsible TEXT, Phone_Email TEXT, StartDate TEXT, Task_Duration INTEGER, IS_TaskComplite INTEGER);");
        statement.executeUpdate("INSERT INTO IncompleteTasks (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                "VALUES ('NotStandardJava', 'Create new Lib', 'Bob', '456-789-00, bob@java.net', '2018-03-20 00:00:00', 180, 0);");
        statement.executeUpdate("INSERT INTO IncompleteTasks (Project, Task, Responsible, Phone_Email, StartDate, Task_Duration, IS_TaskComplite)" +
                "VALUES ('NotStandardJava', 'Test new Lib', 'Lily', '456-789-00, lily@java.net', '2018-07-20 00:00:00', 180, 0);");


    }
    @After
    public void closeTable(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tables.setResultSet(null);
    }


    @Test
    public void createTablesAndInit() throws SQLException {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Project FROM ProjectManagment WHERE Task = 'GUI';");
            while (resultSet.next()) {
                String name = resultSet.getString("Project");
                assertEquals("Browser FirsFast", name);
            }
    }

    @Test
    public void testShowProjectInWork() {
        try  {
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
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (Task) FROM IncompleteTasks WHERE (IS_TaskComplite =0 AND Project='NotStandardJava');");
            int count = resultSet.getInt(1);
            tables.countIncompleteProjectTasks("jdbc:sqlite:test.db", "IncompleteTasks", "NotStandardJava");
            assertEquals(count, tables.getResultSet().getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIncompleteResponsibleTasks() {
        try  {
            ResultSet resultSet = statement.executeQuery("SELECT Task FROM IncompleteTasks WHERE (Responsible='Bob' AND IS_TaskComplite =0);");
            String tasks = resultSet.getString("Task");
            tables.incompleteResponsibleTasks("jdbc:sqlite:test.db", "IncompleteTasks", "Bob");
            assertEquals(tasks, tables.getResultSet().getString("Task"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCurrentTasks() {
        try  {
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT Task, Responsible FROM IncompleteTasks WHERE (StartDate<DATE ('now') AND IS_TaskComplite =0);");
            String tasks = resultSet.getString("Task");
            tables.currentTasks("jdbc:sqlite:test.db", "IncompleteTasks");
            assertEquals(tasks, tables.getResultSet().getString("Task"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLateTasks(){
        try  {
          // ResultSet resultSet = statement.executeQuery("SELECT Responsible, Phone_Email FROM IncompleteTasks WHERE ((StartDate+Task_Duration)<DATE ('now') AND IS_TaskComplite =0);");
            ResultSet resultSet = statement.executeQuery("SELECT Responsible, Phone_Email FROM IncompleteTasks WHERE (((strftime('%s', StartDate)) + (Task_Duration*86400)) < (strftime('%s', 'now')));");
            String name = resultSet.getString("Responsible");
            tables.lateTasks("jdbc:sqlite:test.db", "IncompleteTasks");
            assertEquals(name, tables.getResultSet().getString("Responsible"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}