package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.message;
import com.dev.objects.userObject;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
            userObject userObject = new userObject();
            userObject.setPassword(password);
            userObject.setUsername(username);
            String hash = Utils.createHash(username, password);
            userObject.setToken(hash);
            success= persist.createUser(userObject);
        }
        return success;
    }

    @RequestMapping("MyMessages")
    public List<message> myMessages(String token){
        return  persist.getAllMyMessages(token);
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