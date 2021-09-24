package com.example.final_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart){
        if (newPart != null) {
            if (newPart.getMax() > newPart.getMin()) {
                allParts.add(newPart);
            }
        }
    }
    public static void removePart(Part part){
        allParts.remove(part);
    }

    public static void removeProduct(Product product){
        allProducts.remove(product);
    }


    public static void addProduct(Product newProduct){
        if (newProduct != null) {
            if (newProduct.getMax() > newProduct.getMin()){
                allProducts.add(newProduct);
            }
        }
    }
    public static Part lookupPart(int partID){
        int n = 0;
        for (int i = 0; i < allParts.size(); i++){
            if (allParts.get(i).getId() == partID){
                n = i;
            }
        }
        return allParts.get(n);
    }
    public static ObservableList<Part> lookupPart(String partName){

        ObservableList<Part> allPartsSearch = FXCollections.observableArrayList();
        for (int i = 0; i < Inventory.getAllParts().size(); i++){
            if (Inventory.getAllParts().get(i).getName().equals(partName) || Inventory.getAllParts().get(i).getName().contains(partName)){
                allPartsSearch.add(getAllParts().get(i));
            }
        }
        return allPartsSearch;
    }
    public static Product lookupProduct(int productID){
        int n = 0;
        for (int i = 0; i < allProducts.size(); i++){
            if (allProducts.get(i).getId() == productID){
                n = i;
            }
        }
        return allProducts.get(n);
    }
    public static ObservableList<Product> lookupProduct(String productName){

        ObservableList<Product> allProductsSearch = FXCollections.observableArrayList();
        for (int i = 0; i < Inventory.getAllProducts().size(); i++){
            if (getAllProducts().get(i).getName().equals(productName) || getAllProducts().get(i).getName().contains(productName)){
                allProductsSearch.add(getAllProducts().get(i));
            }
        }
        return allProductsSearch;
    }



    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    public static boolean deletePart(Part selectedPart){
        return selectedPart != null;
    }
    public static boolean deleteProduct(Product selectedProduct) { return selectedProduct != null; }


    public static void updatePart(int index, Part selectedPart){
        if (selectedPart.getMax() > selectedPart.getMin() && selectedPart.getStock() <= selectedPart.getMax() && selectedPart.getStock() >= selectedPart.getMin()) {
            allParts.remove(index);
            allParts.add(selectedPart);
        }
    }
    public static void updateProduct(int index, Product selectedProduct){
        if (selectedProduct.getMax() > selectedProduct.getMin() && selectedProduct.getStock() <= selectedProduct.getMax() && selectedProduct.getStock() >= selectedProduct.getMin()) {
            allProducts.remove(index);
            allProducts.add(selectedProduct);
        }
    }
}
