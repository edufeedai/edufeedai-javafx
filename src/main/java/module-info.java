module com.github.edufeedai{
    requires org.slf4j;
    
    requires java.desktop;
    requires com.google.gson;
    requires java.logging;
    requires java.net.http;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.json;
    requires transitive org.apache.pdfbox;
    requires tess4j;
    requires opencv;
    

    exports com.github.edufeedai;
    exports com.github.edufeedai.model;
    exports com.github.edufeedai.model.openai.platform;
    exports com.github.edufeedai.model.openai.platform.api.batches;
    opens com.github.edufeedai.model.openai.platform to com.google.gson;
    opens com.github.edufeedai.model.openai.platform.response to com.google.gson;
    opens com.github.edufeedai.model.openai.platform.api.batches to com.google.gson;
    exports com.github.edufeedai.model.exceptions;
    opens com.github.edufeedai.model to com.google.gson;
    exports com.github.edufeedai.model.ocrlib; 
    
}
