package com.klipfolio.messagebroker.core.message.textmessage;

import com.klipfolio.messagebroker.core.Message;

public class TextMessage implements Message<String> {

    private String body;

    public TextMessage(String body) {
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }
}
