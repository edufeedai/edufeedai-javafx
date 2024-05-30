module com.github.amiguetes.edufeedai.edufeedaijavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires com.google.gson;
    requires java.net.http;
    requires org.commonmark;

    opens com.github.amiguetes.edufeedai.edufeedaijavafx to javafx.fxml;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx.model;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform;
    opens com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform to com.google.gson;
    opens com.github.amiguetes.edufeedai.edufeedaijavafx.model.openai.platform.response to com.google.gson;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx.model.exceptions;
    opens com.github.amiguetes.edufeedai.edufeedaijavafx.model to javafx.fxml, com.google.gson;

}