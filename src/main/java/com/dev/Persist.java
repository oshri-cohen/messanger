package com.dev;

import com.dev.objects.userObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;

@Component
public class

Persist {
    private Connection connection;


    @PostConstruct
    public void createConnectionToDatabase(){
        try{
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/messenger?useSSL=false","root","1234"
            );
            System.out.println("Connection");
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("Connection");
    }

    public String doesUserExist(String username, String password) {
        String token = null;
        try {
            if (this.doesUsernameExist(username)) {
                int blocks = this.getUserCountLoginFaild(username);
                if (blocks < 5) {
                    createConnectionToDatabase();
                    PreparedStatement preparedStatement = this.connection.prepareStatement(
                            "SELECT token FROM user WHERE username = ? AND password = ?"
                    );
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        token = resultSet.getString("token");
                        this.setUserCountLoginFaild(username, 0);
                    }else {
                        if(blocks+1<5){
                            token = "password";
                            this.setUserCountLoginFaild(username, blocks + 1);
                        }else {
                            token = "block";
                        }
                    }
                } else {
                    token = "block";
                }
            } else {
                token = "username";
            }
            return token;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return token;
}




    public boolean setUserCountLoginFaild(String username, int blocks){
        createConnectionToDatabase();
        boolean success =false;
        try {
            PreparedStatement preparedStatement1 = this.connection.prepareStatement(
                    "UPDATE user SET countLoginFaild=? WHERE username = ?"
            );
            preparedStatement1.setInt(1,blocks);
            preparedStatement1.setString(2,username);
            int resultSet = preparedStatement1.executeUpdate();
            success = resultSet ==1 ? true : false ;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }


    public int getUserCountLoginFaild(String username){
        createConnectionToDatabase();
        try {
            int blocks =-1;
            PreparedStatement preparedStatement3 = this.connection.prepareStatement(
                    "SELECT countLoginFaild FROM user WHERE username = ?"
            );
            preparedStatement3.setString(1, username);
            ResultSet resultSet1 = preparedStatement3.executeQuery();
            if(resultSet1.next()){
                blocks = resultSet1.getInt("countLoginFaild");

            }
            return blocks;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }


    public boolean doesUsernameExist(String username){
        createConnectionToDatabase();
        boolean isExist = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT token FROM user WHERE username = ?"
            );
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                isExist =true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isExist;
    }


    public boolean createUser(userObject userObject){
        boolean done = false;
        try {
            createConnectionToDatabase();
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "INSERT INTO user (username, password, token) VALUES(?,?,?);"
            );
            preparedStatement.setString(1,userObject.getUsername());
            preparedStatement.setString(2,userObject.getPassword());
            preparedStatement.setString(3,userObject.getToken());
            int updates = preparedStatement.executeUpdate();
            done = updates == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return done;
    }



}