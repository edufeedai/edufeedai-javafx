module com.github.amiguetes.edufeedai.edufeedaijavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.github.amiguetes.edufeedai.edufeedaijavafx to javafx.fxml;
    exports com.github.amiguetes.edufeedai.edufeedaijavafx;
}