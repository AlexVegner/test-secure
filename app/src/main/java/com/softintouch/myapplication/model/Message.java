package com.softintouch.myapplication.model;

/**
 * Created by alex on 3/23/17.
 */

public class Message {

    public String data;

    public Message(String encryptedMessage) {
        data = encryptedMessage;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return data != null ? data.equals(message.data) : message.data == null;

    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}
