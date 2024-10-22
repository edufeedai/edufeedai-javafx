package com.github.edufeedai.javafx.model;// Clase para representar un bloque de contenido (texto o imagen)

class ContentBlock {

    private String type; // Puede ser "text" o "image"
    private String content;

    public ContentBlock(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
