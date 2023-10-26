module com.se233.spaceinvader {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires org.apache.logging.log4j;

    opens com.se233.spaceinvader to javafx.fxml;
    exports com.se233.spaceinvader;
    exports com.se233.spaceinvader.controllers;
    exports com.se233.spaceinvader.models;
    exports com.se233.spaceinvader.views;
    exports com.se233.spaceinvader.enums;
    exports com.se233.spaceinvader.views.elements;
    exports com.se233.spaceinvader.models.managers;
}