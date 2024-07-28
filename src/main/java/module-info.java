module com.example.multimedia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires lombok;
    requires JTransforms;
    requires itextpdf;
    requires telegrambots;
    requires telegrambots.meta;

    opens com.example.multimedia to javafx.fxml;
    exports com.example.multimedia;
}