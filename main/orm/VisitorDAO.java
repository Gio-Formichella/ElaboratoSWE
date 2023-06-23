package main.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import main.DomainModel.*;

public class VisitorDAO {
    public ArrayList<Visitor> getNLSubscribers() throws SQLException {
        Connection con = ConnectionManager.getConnection();
        ArrayList<Visitor> nlsubscribers = new ArrayList<>();

        String sql = "SELECT email, name, surname FROM Visitor WHERE newsletter = true";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            String email = rs.getString("email");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            nlsubscribers.add(new Visitor(name, surname, email, true));
        }

        return nlsubscribers;
    }
}
