package com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform;

public class JSONLine {

    String custom_id;
    String method;
    String url;
    Body body;
    int max_tokens;

    public String getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(String custom_id) {
        this.custom_id = custom_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }
}
