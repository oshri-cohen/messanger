package com.dev;

import com.dev.objects.message;
import com.dev.objects.userObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public boolean deleteMessages(int id){
        boolean done = false;
        try {
            createConnectionToDatabase();
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "DELETE FROM messages WHERE messageId =?"
            );
            preparedStatement.setInt(1,id);
            int updates = preparedStatement.executeUpdate();
            done = updates == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return done;
    }

    public boolean markAsRead(int id){
        createConnectionToDatabase();
        System.out.println("mark");
        boolean success =false;
        try {
            PreparedStatement preparedStatement1 = this.connection.prepareStatement(
                    "UPDATE messages SET readTime= now()  WHERE messageId = ?"
            );
            preparedStatement1.setInt(1,id);
            int resultSet = preparedStatement1.executeUpdate();
            success = resultSet ==1 ? true : false ;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }


    public List<message> getAllMyMessages(String token){
        List<message> messages = new ArrayList<>();
        int id = getUserIdByToken(token);
        try {
            createConnectionToDatabase();
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT messenger.messages.* FROM messages JOIN user u on u.id = messages.addresseeId WHERE addresseeId =?"
            );
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                message message = new message();
                message.setSenderId(resultSet.getInt("senderId"));
                message.setAddressId(id);
                message.setBody(resultSet.getString("body"));
                message.setTitle(resultSet.getString("title"));
                message.setReadTime(resultSet.getTime("readTime"));
                message.setSendTime(resultSet.getTime("sendTime"));
                message.setSenderName(this.getUsernameById(resultSet.getInt("senderId")));
                message.setMessageId(resultSet.getInt("messageId"));
                messages.add(message);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    public Integer getUserIdByToken (String token) {
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT id FROM user WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public String getUsernameById (int id) {
        String name ="";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT username FROM user WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
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

    public Integer getUserIdByUsername(String username){
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT id FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public boolean sendMessage(String username, String title, String content, String token){
        boolean success = false;
        try {
            int senderId = this.getUserIdByToken(token);
            int addresseeId = this.getUserIdByUsername(username);
            createConnectionToDatabase();
            PreparedStatement preparedStatement1 = this.connection.prepareStatement(
                        "INSERT INTO messages (senderId, addresseeId, title, body, sendTime) VALUES (?,?,?,?,now()) "
                );
            preparedStatement1.setInt(1, senderId);
            preparedStatement1.setInt(2, addresseeId);
            preparedStatement1.setString(3, title);
            preparedStatement1.setString(4, content);
            int updates = preparedStatement1.executeUpdate();
                if (updates > 0) {
                    success = true;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }


}