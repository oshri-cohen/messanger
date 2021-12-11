package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.Message;
import com.dev.objects.UserObject;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@RestController
public class TestController {


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init () {

    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = persist.doesUserExist(username,password);
        return token;
    }

    @RequestMapping("create-account")
    public boolean createAccount (String username, String password) {
        boolean success = false;
        if (!persist.doesUsernameExist(username)) {
            UserObject userObject = new UserObject();
            userObject.setPassword(password);
            userObject.setUsername(username);
            String hash = Utils.createHash(username, password);
            userObject.setToken(hash);
            success= persist.createUser(userObject);
        }
        return success;
    }

    @RequestMapping("checkIfUserExistByUsername")
    public boolean checkIfUserExist(String username){
        return persist.doesUsernameExist(username);
    }

    @RequestMapping("MyMessages")
    public List<Message> myMessages(String token){
        return  persist.getAllMyMessages(token);
    }

    @RequestMapping("delete-messages")
    public boolean deleteMessage(int id){
        return persist.deleteMessages(id);
    }

    @RequestMapping("markAsRead")
    public boolean markAsRead(int id){
        return persist.markAsRead(id);
    }

    @RequestMapping("sendMessage")
    public boolean sendMessage(String username, String title, String content, String token){
        return persist.sendMessage(username, title, content ,token);
    }

    public static String createHash (String username, String password) {
        String myHash = null;
        try {
            String hash = "35454B055CC325EA1AF2126E27707052";

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((username + password).getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return myHash;
    }


}