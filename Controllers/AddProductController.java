package com.example.final_project;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;


public class AddProductController {

    private int index;
    private Product currentProduct;

    @FXML
    private TextField fieldId;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldPrice;
    @FXML
    private TextField fieldInv;
    @FXML
    private TextField fieldMin;
    @FXML
    private TextField fieldMax;

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
    private TableView<Part> tableView1;
    @FXML
    private TableColumn<Part, Integer> partID1;
    @FXML
    private TableColumn<Part, String> partName1;
    @FXML
    private TableColumn<Part, Integer> invLevel1;
    @FXML
    private TableColumn<Part, Double> pricePerUnit1;

    @FXML
    private Label modifyProductLabel;
    @FXML
    private Label searchWarningLabel;
    @FXML
    private TextField searchPart;

    @FXML
    private Label exceptionLabelString;
    @FXML
    private Label exceptionLabelInv;
    @FXML
    private Label exceptionLabelPrice;
    @FXML
    private Label exceptionLabelMax;
    @FXML
    private Label exceptionLabelMin;


    public void setIndex(int i){
        index = i;
    }
    public int getIndex(){
        return index;
    }

    /**
     * @param product to be loaded later
     * */
    public void setCurrentProduct(Product product){
        currentProduct = product;
    }

    public void addAssociatedPart() {
        Part part = tableView.getSelectionModel().getSelectedItem();
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            tableView1.getItems().add(part);
        }
    }

    public void loadParts(){
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView.setItems(Inventory.getAllParts());
    }
    public void loadTableView1(){
        partID1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevel1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pricePerUnit1.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void loadAssociatedParts(){
        partID1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLevel1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pricePerUnit1.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView1.setItems(currentProduct.getAllAssociatedParts());
    }

    @FXML
    public void cancelButtonAddProductClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1004, 509);
        stage.setScene(scene);
        stage.show();
    }
    public void loadModifyProduct(Product product){

        int id = product.getId();
        double price = product.getPrice();
        int inv = product.getStock();
        int max = product.getMax();
        int min = product.getMin();

        fieldId.setText(Integer.toString(id));
        fieldName.setText(product.getName());
        fieldPrice.setText(Double.toString(price));
        fieldInv.setText(Integer.toString(inv));
        fieldMax.setText(Integer.toString(max));
        fieldMin.setText(Integer.toString(min));
    }

    public void raiseException(ActionEvent event) throws IOException {
        exceptionLabelString.setText("");
        exceptionLabelMin.setText("");
        exceptionLabelMax.setText("");
        exceptionLabelPrice.setText("");
        exceptionLabelInv.setText("");

        String regex = "\\d+";
        boolean b = false;
        if (fieldName.getText().matches(regex) || fieldName.getText().equals("")) {
            exceptionLabelString.setText("Name must contain a valid string.");
            b = true;
        }
        try {
            if (!(fieldInv.getText().matches(regex))) {
                exceptionLabelInv.setText("Inv must contain a valid integer");
                b = true;
            }
        } catch (NumberFormatException e){
            exceptionLabelInv.setText("Inv must contain a valid integer");
            b = true;
        }
        try {
            Double.parseDouble(fieldPrice.getText());
        } catch (NumberFormatException e){
            exceptionLabelPrice.setText("Price must contain a valid double");
            b = true;
        }
        if (!(fieldMax.getText().matches(regex))){
            exceptionLabelMax.setText("Max must contain a valid integer");
            b = true;
        }
        if (!(fieldMin.getText().matches(regex))){
            exceptionLabelMin.setText("Min must contain a valid integer");
            b = true;
        }
        if (!(b)){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1004, 509);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void saveProductButton(ActionEvent event) throws IOException {

        // get the current stage (we need to do this to check if it is the add part stage OR modify part stage, which will change the functionality of the save button)
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        try {
            Stage modifyProductLabelStage;
            modifyProductLabelStage = (Stage) modifyProductLabel.getScene().getWindow();

            if (currentStage == modifyProductLabelStage) {
                try {
                    if (Integer.parseInt(fieldMax.getText()) <= Integer.parseInt(fieldMin.getText()) ||
                            Integer.parseInt(fieldInv.getText()) > Integer.parseInt(fieldMax.getText()) ||
                            Integer.parseInt(fieldInv.getText()) < Integer.parseInt(fieldMin.getText())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Warning");
                        alert.setContentText("Max must be more than Min, and Inventory level must be between Min and Max, inclusive.");
                        alert.show();
                    }
                    else if (fieldName.getText().matches("\\d+")) {
                        raiseException(event);
                    }
                    else {
                        Product product = new Product(Integer.parseInt(fieldId.getText()), fieldName.getText(), Double.parseDouble(fieldPrice.getText()),
                                Integer.parseInt(fieldInv.getText()), Integer.parseInt(fieldMin.getText()),
                                Integer.parseInt(fieldMax.getText()));
                        Inventory.updateProduct(getIndex(), product);
                        // save associated parts from tableview1
                        for (Part part : tableView1.getItems()) {
                            product.addAssociatedPart(part);
                        }
                       raiseException(event);
                    }
                } catch (Exception e){
                    raiseException(event);
                }
            }

        } catch (NullPointerException nullPointerException){
            try {
                if (Integer.parseInt(fieldMax.getText()) <= Integer.parseInt(fieldMin.getText()) ||
                        Integer.parseInt(fieldInv.getText()) > Integer.parseInt(fieldMax.getText()) ||
                        Integer.parseInt(fieldInv.getText()) < Integer.parseInt(fieldMin.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Warning");
                    alert.setContentText("Max must be more than Min, and Inventory level must be between Min and Max, inclusive.");
                    alert.show();
                }
                else if (fieldName.getText().matches("\\d+")) {
                    raiseException(event);
                }
                else {
                    System.out.println("Code has been reached.");
                    Product product = new Product(generateID(), fieldName.getText(), Double.parseDouble(fieldPrice.getText()),
                            Integer.parseInt(fieldInv.getText()), Integer.parseInt(fieldMin.getText()),
                            Integer.parseInt(fieldMax.getText()));
                    Inventory.addProduct(product);
                    // save associated parts from tableview1
                    for (Part part : tableView1.getItems()) {
                        product.addAssociatedPart(part);
                    }
                    raiseException(event);
                }
            } catch (Exception e){
                raiseException(event);
            }
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
    public void deletePartButtonClick(){

        if (tableView1.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this associated part?", ButtonType.OK, ButtonType.CANCEL);
            alert.setHeaderText("Delete associated Part");
            Optional<ButtonType> confirm = alert.showAndWait();
            if (confirm.isEmpty()) {
                {}
            }
            else if (confirm.get() == ButtonType.OK) {
                int ind = tableView1.getSelectionModel().getSelectedIndex();
                tableView1.getItems().remove(ind);
                //
            }
            else if (confirm.get() == ButtonType.CANCEL){
                {}
            }
        }
    }

    public int generateID(){

        int lastId = 0;
        int size = Inventory.getAllProducts().size();
        for (int i = 0; i < size; i++){
            if (Inventory.getAllProducts().get(i).getId() >= lastId){
                lastId = Inventory.getAllProducts().get(i).getId();
            }
        }
        lastId++;
        return lastId;
    }
}
