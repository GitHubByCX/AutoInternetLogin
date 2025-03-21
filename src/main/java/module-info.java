module org.cx.autointernetlogin {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.yaml.snakeyaml;
    requires java.logging;
    requires java.desktop;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    opens org.cx.autointernetlogin.view to javafx.fxml;
    exports org.cx.autointernetlogin;
    exports org.cx.autointernetlogin.config;
}