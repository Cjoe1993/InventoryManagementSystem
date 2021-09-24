package com.example.final_project;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    @FXML
    private TableView<Part> tableView;
    @FXML
    private TableColumn<Part, Integer> partID;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> invLevel;
    @FXML
    private TableColumn<Part, Double> pricePerUnit;
    @FXML
    private TableView<Product> tableView1;
    @FXML
    private TableColumn<Product, Integer> productID;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> stockLevel;
    @FXML
    private TableColumn<Product, Double> pricePerUnit1;
    @FXML
    private TextField searchPart;
    @FXML
    private TextField searchProduct;
    @FXML
    private Label searchWarningLabel;
    @FXML
    private Label searchWarningLabel2;

    @FXML
    private Label cannotDeleteProductLabel;

    /**
    * @throws
    * IOException was added after IDE Recommendation
    * */
    @FXML
    public void addButtonClick(ActionEvent event) throws IOException {
        // this code changes between different windows by loading the new windows fxml page
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-part.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1004, 509);
        stage.setScene(scene);
        stage.show();
    }
    public void addProductButtonClick(ActionEvent event) throws IOException {

        Stage stage;
        Parent root;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add-product.fxml"));
        root = fxmlLoader.load();
        AddProductController addProductController = fxmlLoader.getController();
        addProductController.loadParts();
        addProductController.loadTableView1();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void deletePartButtonClick(){
        // delete part from ObservableArray
        // refresh the table
        if (Inventory.deletePart(tableView.getSelectionModel().getSelectedItem())){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Delete Part");
            Optional<ButtonType> confirm = alert.showAndWait();
            if (confirm.isEmpty()) {
                {}
            }
            else if (confirm.get() == ButtonType.OK) {
                Inventory.removePart(tableView.getSelectionModel().getSelectedItem());
            }
            else if (confirm.get() == ButtonType.CANCEL){
                {}
            }
        }
        tableView.setItems(Inventory.getAllParts());
        tableView.getSelectionModel().clearSelection();
    }
    public void deleteProductButtonClick(){

        cannotDeleteProductLabel.setText("");

        if (Inventory.deleteProduct(tableView1.getSelectionModel().getSelectedItem())){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Delete Product");
            Optional<ButtonType> confirm = alert.showAndWait();
            if (confirm.isEmpty()) {
                {}
            }
            else if (confirm.get() == ButtonType.OK) {
                if (tableView1.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() == 0) {
                    Inventory.removeProduct(tableView1.getSelectionModel().getSelectedItem());
                }
                else {
                    cannotDeleteProductLabel.setText("This product has associated parts and cannot be deleted.");
                }
            }
            else if (confirm.get() == ButtonType.CANCEL){
                {}
            }
        }
        tableView1.setItems(Inventory.getAllProducts());
        tableView1.getSelectionModel().clearSelection();
    }
    @FXML
    public void modifyPartButtonClick(ActionEvent event) throws IOException {

        Stage stage;
        Parent root;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("modify-part.fxml"));
        root = fxmlLoader.load();

        // access the controller before the page loads...
        // this took me 3 hours to figure out how to do as I was using the default javafx loading expressions, which were incompatible with using getController().
        AddPartController addPartController = fxmlLoader.getController();
        Part part = tableView.getSelectionModel().getSelectedItem();

        // feed the index of the part to delete into the AddPartController index variable
        int pos = tableView.getSelectionModel().getSelectedIndex();
        addPartController.setIndex(pos);
        if (!(part == null)){
            addPartController.loadModifyPart(part); // feeding addpartcontroller the right part to then load into text fields
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
    @FXML
    public void modifyProductButtonClick(ActionEvent event) throws IOException {

        Stage stage;
        Parent root;
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("modify-product.fxml"));
        root = fxmlLoader.load();

        AddProductController addProductController = fxmlLoader.getController();
        addProductController.loadParts();
        Product product = tableView1.getSelectionModel().getSelectedItem();
        addProductController.setCurrentProduct(product);
        addProductController.loadAssociatedParts();

        int pos = tableView1.getSelectionModel().getSelectedIndex();
        addProductController.setIndex(pos);
        if (!(product == null)){
            addProductController.loadModifyProduct(product);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void searchPartTextFieldEnter(){

        searchWarningLabel.setText("");

        try {
            int j = Integer.parseInt(searchPart.getText());
            // this works because it is returning a single part
            tableView.getSelectionModel().select(Inventory.lookupPart(j));
        }
        catch (NumberFormatException ex) {

            // grab string from text field, create new list each time method is invoked containing matching parts if any.
            String str = searchPart.getText();
            ObservableList<Part> searchParts = Inventory.lookupPart(str);

            if (searchParts.size() == Inventory.getAllParts().size()){
                tableView.setItems(Inventory.getAllParts());
            }
            else if (searchParts.size() < Inventory.getAllParts().size() && searchParts.size() > 0){
                tableView.setItems(searchParts);
            }
            else {
                searchWarningLabel.setText("No results");
            }
        }
        if (searchPart.getText().isEmpty()) {
            searchWarningLabel.setText("All parts");
            tableView.getSelectionModel().clearSelection();
        }
    }

    public void searchProductTextFieldEnter(){

        searchWarningLabel2.setText("");

        try {
            int j = Integer.parseInt(searchProduct.getText());
            tableView1.getSelectionModel().select(Inventory.lookupProduct(j));
        }
        catch (NumberFormatException ex) {
            String str = searchProduct.getText();
            ObservableList<Product> searchProducts = Inventory.lookupProduct(str);

            if (searchProducts.size() == Inventory.getAllProducts().size()){
                tableView1.setItems(Inventory.getAllProducts());
            }
            else if (searchProducts.size() < Inventory.getAllProducts().size() && searchProducts.size() > 0){
                tableView1.setItems(searchProducts);
            }
            else {
                searchWarningLabel2.setText("No results");
            }
        }
        if (searchProduct.getText().isEmpty()) {
            searchWarningLabel2.setText("All products");
            tableView1.getSelectionModel().clearSelection();
        }
    }



    public void exitButtonClick() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView.setItems(Inventory.getAllParts());

        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pricePerUnit1.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView1.setItems(Inventory.getAllProducts());

    }
}