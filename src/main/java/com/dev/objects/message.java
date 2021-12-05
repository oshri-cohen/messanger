
package com.dev.objects;
import java.util.Date;

public class message {
    int senderId;
    String senderName;
    int addressId;
    String title;
    String body;
    Date sendTime;
    Date readTime;


    public message(){
    }


    public message(int senderId, int addressId, String title, String body, Date sendTime, Date readTime , String senderName) {
        this.senderId = senderId;
        this.addressId = addressId;
        this.title = title;
        this.body = body;
        this.sendTime = sendTime;
        this.readTime = readTime;
        this.senderName = senderName;
    }

    public message(message message){
        this.senderId = message.senderId;
        this.senderName = message.senderName;
        this.addressId = message.addressId;
        this.title = message.title;
        this.body = message.body;
        this.sendTime = message.sendTime;
        this.readTime = message.readTime;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
}
