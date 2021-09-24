package com.example.final_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // add sample parts and products
        // future enhancement: load data from csv files or database
        Inventory.addPart(new InHouse(0, "Rubber Case", 10.0, 5, 1, 10, 0));
        Inventory.addPart(new InHouse(1, "Copper Coil", 2.0, 7, 1, 10, 17));

        Inventory.addProduct(new Product(0, "Lenovo PC", 800.0, 7, 1, 10));
        Inventory.addProduct(new Product(1, "Samsung Nvme ssd", 120.0, 2, 1, 10));


        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("WGU Inventory Management System");
        stage.setMinHeight(509.0);
        stage.setMinWidth(1004.0);
        stage.setScene(scene);
        stage.show();
    }
    // Javadoc generated files/directories are located in the directory: javadoc_generated_files
    public static void main(String[] args) {
        launch();
    }
}