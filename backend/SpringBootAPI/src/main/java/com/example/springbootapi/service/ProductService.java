package com.example.springbootapi.service;

import com.example.springbootapi.api.model.Product;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductService {

    Connection connection;
    ResultSet resultSet;
    ArrayList<Product> products = new ArrayList<>();

    private void sqlQuery(){
        try {
            String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db";
            connection = DriverManager.getConnection(url);

            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery("select * from products");
            while (resultSet.next()){
                products.add(
                        new Product(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getDouble(3)
                        ));
            }
        } catch (Exception err) {
            System.out.println("------------sqlQuery()----------------" + err);
        }
    }

    public Optional getProduct(String id) {
        try {
            sqlQuery();
            Optional<Product> optionalProduct = Optional.empty();

            for (Product product:products) {
                if(product.getId().equals(id)){
                    optionalProduct = Optional.of(product);
                    return optionalProduct;
                }
            }
            return null;
        } catch (Exception err) {
            System.out.println("------------getProduct("+id+")----------------" + err);
        }
        return null;
    }
}