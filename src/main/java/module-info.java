module com.github.amiguetes.edufeedai.edufeedaijavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.desktop;

    opens com.github.amiguetes.edufeedai.edufeedaijavafx to javafx.fxml;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx.model;
    opens com.github.amiguetes.edufeedai.edufeedaijavafx.model to javafx.fxml;
}