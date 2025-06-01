/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.History;

/**
 *
 * @author thais
 */
public class HistoryDAO {

    public static ArrayList<History> getHistorys() {
        DBContext db = DBContext.getInstance();

        ArrayList<History> history = new ArrayList<>();

        try {
            String sql = """
                         select * from History
                         """;
            PreparedStatement statement = db.getConnection().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                History h = new History(rs.getInt("historyID"),
                        rs.getInt("userID"),
                        rs.getString("action"),
                        rs.getString("changeDate"));
                history.add(h);
            }
        } catch (Exception e) {
            return null;
        }
        return history;
    }

    public static History deleteHistory(History history) {
        DBContext db = DBContext.getInstance(); // (1)
        int rs = 0;
        try {
            String sql = """
            delete from History
            where historyID = ?
            """; // (2)
            PreparedStatement statment = db.getConnection().prepareStatement(sql); // (3)
            statment.setInt(1, history.getHistoryID()); // (4)
            rs = statment.executeUpdate(); // (5)
        } catch (Exception e) {
            return null;
        }
        if (rs == 0) {
            return null;
        } else {
            return history;
        }
    }

    public static History insertHistory(History history) {
        DBContext db = DBContext.getInstance();
        int rs = 0;
        try {
            String sql = """
            INSERT INTO History (userID, action, changeDate)
            VALUES (?, ?, ?)
            """;

            PreparedStatement statement = db.getConnection().prepareStatement(sql);

            statement.setInt(1, history.getUserID());
            statement.setString(2, history.getAction());
            statement.setString(3, history.getChangeDate());

            rs = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
