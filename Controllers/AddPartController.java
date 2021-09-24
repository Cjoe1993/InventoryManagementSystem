package com.example.final_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


public class AddPartController {

    private int index;

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
    private TextField machineID;
    @FXML
    private TextField companyName;
    @FXML
    public ToggleGroup addPartsGroup;
    @FXML
    private RadioButton radioOutsourced;
    @FXML
    private RadioButton radioInHouse;
    @FXML
    private Label modifyPartLabel;
    @FXML
    private Label exceptionLabelString;
    @FXML
    private Label exceptionLabelInv;
    @FXML
    private Label exceptionLabelPrice;
    @FXML
    private Label exceptionLabelMax;
    @FXML
    private Label exceptionLabelMachId;
    @FXML
    private Label exceptionLabelMin;
    @FXML
    private Label exceptionLabelCompName;

    /**
     * @param i set the index to i
     * */
    public void setIndex(int i){
        index = i;
    }
    /**
     * @return the index
     * */
    public int getIndex(){
        return index;
    }
    /**
     * @param part check if part is inhouse or outsourced, load accordingly
     * */
    public void loadModifyPart(Part part){

        if (radioOutsourced.isPressed()){
            companyName.setDisable(false);
            machineID.setDisable(true);
        }
        else {
            companyName.setDisable(true);
            machineID.setDisable(false);
        }

        if (part instanceof InHouse) {
            addPartsGroup.selectToggle(radioInHouse);
            fieldId.setDisable(true);
            InHouse part2 = (InHouse) part;
            int id = part2.getId();
            double price = part2.getPrice();
            int inv = part2.getStock();
            int max = part2.getMax();
            int min = part2.getMin();
            int machId = part2.getMachineID();

            fieldId.setText(Integer.toString(id));
            fieldName.setText(part.getName());
            fieldPrice.setText(Double.toString(price));
            fieldInv.setText(Integer.toString(inv));
            fieldMax.setText(Integer.toString(max));
            fieldMin.setText(Integer.toString(min));
            machineID.setText(Integer.toString(machId));
        }
        else {
            addPartsGroup.selectToggle(radioOutsourced);
            companyName.setDisable(false);
            machineID.setDisable(true);
            fieldId.setDisable(true);
            Outsourced part2 = (Outsourced) part;
            int id = part2.getId();
            double price = part2.getPrice();
            int inv = part2.getStock();
            int max = part2.getMax();
            int min = part2.getMin();

            fieldId.setText(Integer.toString(id));
            fieldName.setText(part.getName());
            fieldPrice.setText(Double.toString(price));
            fieldInv.setText(Integer.toString(inv));
            fieldMax.setText(Integer.toString(max));
            fieldMin.setText(Integer.toString(min));
            companyName.setText(part2.getCompanyName());
        }

    }
    @FXML
    public void cancelButtonAddPartClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1004, 509);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void radio1OutsourcedClick(){
        companyName.setDisable(false);
        machineID.setDisable(true);
        machineID.setText("");
    }
    @FXML
    public void radio1InHouseClick(){
        companyName.setDisable(true);
        machineID.setDisable(false);
        companyName.setText("");
    }
    public void raiseException(ActionEvent event) throws IOException {
        exceptionLabelString.setText("");
        exceptionLabelCompName.setText("");
        exceptionLabelMin.setText("");
        exceptionLabelMax.setText("");
        exceptionLabelPrice.setText("");
        exceptionLabelMachId.setText("");
        exceptionLabelInv.setText("");

        // regex represengint 0-9
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
        if (!(machineID.getText().matches(regex)) && radioInHouse.isSelected()){
            exceptionLabelMachId.setText("Machine Id must contain a valid integer");
            b = true;
        }
        if (companyName.getText().matches(regex) || companyName.getText().equals("") && radioOutsourced.isSelected()){
            exceptionLabelCompName.setText("Company name must contain a valid string");
            b = true;
        }
        if (!(b)){
            // if everything is correct, load main app page, otherwise throw error
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1004, 509);
            stage.setScene(scene);
            stage.show();
        }
    }
    @FXML
    public void savePartButton(ActionEvent event) throws IOException {

        // get the current stage (we need to do this to check if it is the add part stage OR modify part stage, which will change the functionality of the save button)
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();

        try {
            // This code grabs the stage which is pulled from an invisible label in the modify parts stage. If it equals the current stage, then the save button uses
            // the modify parts functionality, else it defaults to the add parts functionality when a null-pointer exception is raised for the label.
            Stage modifyPartLabelStage;
            modifyPartLabelStage = (Stage) modifyPartLabel.getScene().getWindow();


            if (currentStage == modifyPartLabelStage && companyName.isDisabled()) {
                // check that max is higher than min, warn accordingly
                try {
                    if (Integer.parseInt(fieldMax.getText()) <= Integer.parseInt(fieldMin.getText()) ||
                            Integer.parseInt(fieldInv.getText()) > Integer.parseInt(fieldMax.getText()) ||
                            Integer.parseInt(fieldInv.getText()) < Integer.parseInt(fieldMin.getText())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Warning");
                        alert.setContentText("Max must be more than Min, and Inventory level must be between Min and Max, inclusive.");
                        alert.show();
                    }
                    else if (fieldName.getText().matches("\\d+")){
                        raiseException(event);
                    }
                    else {
                        InHouse partInHouse = new InHouse(Integer.parseInt(fieldId.getText()), fieldName.getText(), Double.parseDouble(fieldPrice.getText()),
                                Integer.parseInt(fieldInv.getText()), Integer.parseInt(fieldMin.getText()),
                                Integer.parseInt(fieldMax.getText()), Integer.parseInt(machineID.getText()));
                        Inventory.updatePart(getIndex(), partInHouse);

                        raiseException(event);
                    }
                } catch (Exception e){
                    raiseException(event);
                }
            }

            else {
                try {
                    if (Integer.parseInt(fieldMax.getText()) <= Integer.parseInt(fieldMin.getText()) ||
                            Integer.parseInt(fieldInv.getText()) > Integer.parseInt(fieldMax.getText()) ||
                            Integer.parseInt(fieldInv.getText()) < Integer.parseInt(fieldMin.getText())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Warning");
                        alert.setContentText("Max must be more than Min, and Inventory level must be between Min and Max, inclusive.");
                        alert.show();
                    }
                    else if (fieldName.getText().matches("\\d+")){
                        raiseException(event);
                    }
                    else {
                        Outsourced partOutsourced = new Outsourced(Integer.parseInt(fieldId.getText()), fieldName.getText(), Double.parseDouble(fieldPrice.getText()),
                                Integer.parseInt(fieldInv.getText()), Integer.parseInt(fieldMin.getText()),
                                Integer.parseInt(fieldMax.getText()), companyName.getText());
                        Inventory.updatePart(getIndex(), partOutsourced);

                        raiseException(event);
                    }
                } catch (Exception e){
                    raiseException(event);
                }
            }
        } catch (NullPointerException nullPointerException){
            if (companyName.isDisabled()) {
                try {
                    if (Integer.parseInt(fieldMax.getText()) <= Integer.parseInt(fieldMin.getText()) ||
                            Integer.parseInt(fieldInv.getText()) > Integer.parseInt(fieldMax.getText()) ||
                            Integer.parseInt(fieldInv.getText()) < Integer.parseInt(fieldMin.getText())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Warning");
                        alert.setContentText("Max must be more than Min, and Inventory level must be between Min and Max, inclusive.");
                        alert.show();
                    }
                    else if (fieldName.getText().matches("\\d+")){
                        raiseException(event);
                    }
                    else {
                        InHouse partInHouse = new InHouse(generateID(), fieldName.getText(), Double.parseDouble(fieldPrice.getText()),
                                Integer.parseInt(fieldInv.getText()), Integer.parseInt(fieldMin.getText()),
                                Integer.parseInt(fieldMax.getText()), Integer.parseInt(machineID.getText()));
                        Inventory.addPart(partInHouse);

                        raiseException(event);
                    }
                } catch (Exception e){
                    raiseException(event);
                }
            }
            else {
                try {
                    if (Integer.parseInt(fieldMax.getText()) <= Integer.parseInt(fieldMin.getText()) ||
                            Integer.parseInt(fieldInv.getText()) > Integer.parseInt(fieldMax.getText()) ||
                            Integer.parseInt(fieldInv.getText()) < Integer.parseInt(fieldMin.getText())) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText("Warning");
                        alert.setContentText("Max must be more than Min, and Inventory level must be between Min and Max, inclusive.");
                        alert.show();
                    }
                    else if (fieldName.getText().matches("\\d+")){
                        raiseException(event);
                    }
                    else {
                        Outsourced partOutsourced = new Outsourced(generateID(), fieldName.getText(), Double.parseDouble(fieldPrice.getText()),
                                Integer.parseInt(fieldInv.getText()), Integer.parseInt(fieldMin.getText()),
                                Integer.parseInt(fieldMax.getText()), companyName.getText());
                        Inventory.addPart(partOutsourced);

                        raiseException(event);
                    }
                } catch (Exception e){
                    raiseException(event);
                }
            }
        }
    }
    public int generateID(){

        int lastId = 0;
        int size = Inventory.getAllParts().size();
        for (int i = 0; i < size; i++){
            if (Inventory.getAllParts().get(i).getId() >= lastId){
                lastId = Inventory.getAllParts().get(i).getId();
            }
        }
        // increment lastId to be 1 higher than the highest part Id
        lastId++;
        return lastId;
    }
}
