import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class TablesTest {

    @Before
    public void setUp() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Tables tables = new Tables();
        tables.createTablesAndInit("jdbc:sqlite:test.db");
    }

    @Test
    public void createTablesAndInit() throws SQLException {
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:test.db")){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Project FROM ProjectManagment WHERE Task = 'GUI';");
            while (resultSet.next()){
                String name = resultSet.getString("Project");
                assertEquals("Browser FirsFast", name);
            }
        }
    }
}