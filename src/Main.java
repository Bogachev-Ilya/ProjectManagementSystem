public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Tables tables = new Tables();
        tables.createTablesAndInit("jdbc:sqlite:psm.db");
    }
}
