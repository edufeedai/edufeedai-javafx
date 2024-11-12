module com.github.amiguetes.edufeedai.edufeedaijavafx {
    
    requires java.desktop;
    requires com.google.gson;
    requires java.logging;
    requires java.net.http;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.json;
    requires org.apache.pdfbox;
    requires tess4j;
    requires opencv;

    exports com.github.edufeedai.javafx;
    exports com.github.edufeedai.javafx.model;
    exports com.github.edufeedai.javafx.model.openai.platform;
    exports com.github.edufeedai.javafx.model.openai.platform.api.batches;
    opens com.github.edufeedai.javafx.model.openai.platform to com.google.gson;
    opens com.github.edufeedai.javafx.model.openai.platform.response to com.google.gson;
    opens com.github.edufeedai.javafx.model.openai.platform.api.batches to com.google.gson;
    exports com.github.edufeedai.javafx.model.exceptions;
    opens com.github.edufeedai.javafx.model to com.google.gson;

}