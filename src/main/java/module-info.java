module com.example.coursework_uni {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;

    opens com.example.coursework_uni to javafx.base, javafx.fxml;
    exports com.example.coursework_uni;
}