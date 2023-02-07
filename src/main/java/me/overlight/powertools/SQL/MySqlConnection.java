package me.overlight.powertools.SQL;

import me.overlight.powertools.PowerTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {

    public MySqlConnection(String username, String password, String host, String port, String database){
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.db = database;
    }

    private String host, port, db, username, password;

    private Connection conn = null;
    public boolean isConnected() {
        return conn != null;
    }

    public boolean connect() {
        if(!isConnected()) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false", username, password);
                return true;
            } catch(Exception e){
                return false;
            }
        }
        return false;
    }

    public boolean disconnect() {
        if(isConnected()) {
            try {
                conn.close();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    public Connection getConnection(){
        return this.conn;
    }
}
