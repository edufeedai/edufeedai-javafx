package com.github.edufeedai.javafx.model.openai.platform;

public class Body {

    String model;
    Message[] messages;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }
}
