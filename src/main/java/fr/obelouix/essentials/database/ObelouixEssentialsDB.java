package fr.obelouix.essentials.database;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.config.Config;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObelouixEssentialsDB {

    private static ObelouixEssentialsDB instance;
    private static Connection connection;

    private ObelouixEssentialsDB() {
    }

    public static ObelouixEssentialsDB getInstance() {
        if (instance == null) instance = new ObelouixEssentialsDB();
        return instance;
    }

    public void connect() throws SQLException, ClassNotFoundException {

        final String database = Essentials.getInstance().getConfig().getString("mysql-database");
        if (connection == null || connection.isClosed()) {
            if (Objects.equals(Essentials.getInstance().getConfig().getString("database-type"), "SQLite")) {
                final @NotNull File databaseFile = new File(new File(Essentials.getInstance().getDataFolder(), database + ".db").getAbsolutePath());
                connection = DriverManager.getConnection("jdbc:sqlite:/"
                        + databaseFile);
                initDatabase();
            } else if (Objects.equals(Essentials.getInstance().getConfig().getString("database-type"), "MySQL")) {

                final String addr = Essentials.getInstance().getConfig().getString("mysql-address");
                final String port = Essentials.getInstance().getConfig().getString("mysql-port");
                final String user = Essentials.getInstance().getConfig().getString("mysql-user");
                final String pwd = Essentials.getInstance().getConfig().getString("mysql-password");

                connection = DriverManager.getConnection("jdbc:mysql://" + addr + ":" + port
                        + "/" + database, user, pwd);
                initDatabase();
            } else {
                Essentials.getInstance().getLOGGER().severe("Wrong database type in the config. Please set it to : 'SQLite' or 'MySQL'");
                // disable the plugin to force user to correct the 'database-type' setting
                Essentials.getInstance().getPluginLoader().disablePlugin(Essentials.getInstance());
            }
        }

    }

    public void close() throws SQLException {
        connection.close();
        connection = null;
    }

    public void executeQuery(String SQLQuery) throws SQLException, ClassNotFoundException {
        connect();
        final Statement statement = connection.createStatement();
        statement.execute(SQLQuery);
        close();
    }

    private void initDatabase() throws SQLException {
        final List<String> SQL_COMMANDS = new ArrayList<>();
        final Statement statement = connection.createStatement();

        SQL_COMMANDS.add("CREATE TABLE IF NOT EXISTS 'players' ('name' VARCHAR(16), 'UUID' VARCHAR(36), PRIMARY KEY ('UUID'));");
        SQL_COMMANDS.add("CREATE TABLE IF NOT EXISTS 'connection_history'('ID' INTEGER PRIMARY KEY AUTOINCREMENT,'UUID' VARCHAR(36), 'logon' DATETIME ,'logout' DATETIME);");

        System.out.println(Config.isEconomyEnabled);
        if(Config.isEconomyEnabled){
            SQL_COMMANDS.add("CREATE TABLE IF NOT EXISTS 'economy'('UUID' VARCHAR(36) PRIMARY KEY, 'money' REAL);");
        }

        for (final String SQL_COMMAND : SQL_COMMANDS){
            statement.execute(SQL_COMMAND);
        }
    }

}
