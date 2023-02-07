package me.overlight.powertools.SQL;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlOptions {
    public static boolean createNewTable(MySqlConnection sql, String tableName, SqlVar... items){
        String action = "CREATE TABLE IF NOT EXISTS " + tableName;
        try{
            sql.getConnection().prepareStatement(action + " " + SqlVar.getAsSqlAction(items[0].getName(), items)).executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean createNewPlayer(MySqlConnection sql, String tableName, String key, String value, Player player) {
        try {
            if(!playerDataExists(sql, tableName, key, value)){
                PreparedStatement ps2 = sql.getConnection().prepareStatement("INSERT IGNORE INFO " + tableName + " (NAME," + key + ") VALUES (?,?)");
                ps2.setString(1, player.getName());
                ps2.setString(2, value);
                ps2.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean playerDataExists(MySqlConnection sql, String tableName, String key, String value) throws SQLException {
        PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM " + tableName + " WHERE " + key + "=?");
        ps.setString(1, value);
        ResultSet result = ps.executeQuery();
        return result.next();
    }

    public static boolean setPlayerValue(MySqlConnection sql, String tableName, String key, String value, Player player) {
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("UPDATE " + tableName + " SET " + key + "=? WHERE NAME=?");
            ps.setString(1, value);
            ps.setString(2, player.getName());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Object getPlayerValue(MySqlConnection sql, String tableName, String key, Player player, String type) throws SQLException {
        PreparedStatement ps = sql.getConnection().prepareStatement("SELECT " + key + " FROM " + tableName + " WHERE NAME=?");
        ps.setString(1, player.getName());
        ResultSet result = ps.executeQuery();
        if(result.next()){
            switch(type.toLowerCase()){
                case "integer": return result.getInt(key);
                case "string": return result.getString(key);
                case "boolean": return result.getBoolean(key);
                case "date": return result.getDate(key);
                case "byte": return result.getByte(key);
                case "double": return result.getDouble(key);
                case "float": return result.getFloat(key);
                case "time": return result.getTime(key);
            }
        }
        return null;
    }

    public static boolean clearDataBaseTable(MySqlConnection sql, String tableName){
        try {
            sql.getConnection().prepareStatement("TRUNCATE " + tableName).executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean removePlayerData(MySqlConnection sql, String tableName, String key, String value){
        try {
            PreparedStatement ps = sql.getConnection().prepareStatement("DELETE FROM " + tableName + " WHERE " + key + " =?");
            ps.setString(1, value);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
