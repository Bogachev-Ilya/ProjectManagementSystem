public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Tables tables = new Tables();
        String url ="jdbc:sqlite:psm.db";
        String tableName = "ProjectManagment";
        String project = "Android App";
        tables.createTablesAndInit(url, tableName);
        tables.showProjectInWork(url, tableName );
        tables.countIncompleteProjectTasks(url, tableName, project);
    }
}
