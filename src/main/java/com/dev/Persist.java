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

    public String doesUserExist(String username, String password){
        String token = null;
        try {
            createConnectionToDatabase();
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT token FROM user WHERE username = ? AND password = ?"
            );
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                token = resultSet.getString("token");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
    }


    public boolean doesUsernameExist(String username){
        boolean isExist = false;
        try {
            createConnectionToDatabase();
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