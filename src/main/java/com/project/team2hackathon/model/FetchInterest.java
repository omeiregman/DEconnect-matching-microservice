/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.team2hackathon.model;

/**
 *
 * @author Oladipo Samuel
 */
import java.sql.*;
import java.nio.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.project.team2hackathon.controller.InterestMatcher;
import javax.sql.rowset.*;

public class FetchInterest {
    private int userId;
    private String url, username, password, database;
    private Connection con;
    
    public FetchInterest(int userId)throws SQLException, IOException{
        Properties props = new Properties();
        this.userId = userId;
        Path path = Paths.get(System.getProperty("user.dir")).resolve(("ij.properties"));
        System.out.println(path.toString());
        try{
//            props.load(in);
//            String drivers = props.getProperty("jdbc.drivers");
//            if(drivers != null) System.setProperty("jdbc.drivers", drivers);
//            this.url = props.getProperty("jdbc.url");
//            this.username = props.getProperty("jdbc.username");
//            this.password = props.getProperty("jdbc.password");
//            this.database = props.getProperty("jdbc.database");
//            this.con = DriverManager.getConnection(this.url, this.username, this.password); 
            
            
            String drivers = "com.mysql.jdbc.Driver";
            if(drivers != null) System.setProperty("jdbc.drivers", drivers);
            this.url = "jdbc:mysql://40.121.9.10:3306/admin_deconnect";
            this.username = "user_deconnect";
            this.password = "user_deconnect123";
            this.con = DriverManager.getConnection(this.url, this.username, this.password); 
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void setQuery(String query) throws SQLException, IOException{
        Statement state = this.con.createStatement();
        state.executeUpdate(query);
    }
    
    public ResultSet getQuery(String query) throws SQLException, IOException{
        Statement state = this.con.createStatement();
        ResultSet result = state.executeQuery(query);
        
        return result;
    }
    
    /*public InterestMatcher getMatcher(ResultSet resultUser, ResultSet resultAll)throws SQLException{
        InterestMatcher matcher;
        RowSetFactory reUser = RowSetProvider.newFactory();
        CachedRowSet crs = reUser.createCachedRowSet();
        crs.populate(resultUser);

        //crs.

        return matcher;
    }*/
}





