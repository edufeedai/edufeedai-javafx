module com.github.amiguetes.edufeedai.edufeedaijavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires com.google.gson;
    requires java.logging;
    requires java.net.http;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.json;

    opens com.github.amiguetes.edufeedai.edufeedaijavafx to javafx.fxml;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx.model;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform;
    opens com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform to com.google.gson;
    opens com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.response to com.google.gson;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx.model.exceptions;
    opens com.github.amiguetes.edufeedai.edufeedaijavafx.model to javafx.fxml, com.google.gson;

}