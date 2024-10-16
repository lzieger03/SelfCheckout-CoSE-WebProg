package com.example.springbootapi.service;

import com.example.springbootapi.api.controller.ReceiptController;
import com.example.springbootapi.api.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductService {

    Connection connection;
    ResultSet resultSet;
    //ArrayList<Product> products = new ArrayList<>();

//    private void sqlQuery(){
//        try {
//            String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db"; // URL of DB with DB-Dialect
//            connection = DriverManager.getConnection(url); // establish connection to DB
//
//            Statement statement = connection.createStatement();
//
//            resultSet = statement.executeQuery("select * from products"); // sql-call (not optional, because it selects all and puts it into a list)
//            while (resultSet.next()){
//                products.add(
//                        new Product(
//                                resultSet.getString(1),
//                                resultSet.getString(2),
//                                resultSet.getDouble(3)
//                        ));
//            }
//        } catch (Exception err) {
//                        logEvents(
//                    this.getClass().getName(),
//                    new Throwable().getStackTrace()[0].getMethodName(),
//                    err
//            );
//        }
//    }

    private Product sqlGetProductByID(String id){
        try {
            String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/item_database.db"; // URL of DB with DB-Dialect
            connection = DriverManager.getConnection(url); // establish connection to DB

            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery("select * from products where ItemNo=" + id); // sql-call (not optional, because it selects all and puts it into a list)
            if(resultSet.getString(1).equals(id)){
                return new Product(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3)
                );
            }
            return null;
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
            return null;
        }
    }

    public Optional getProduct(String id) {
        try {
            // SQL Select by ID Method:
            Optional<Product> optionalProduct = Optional.empty(); // Redundant initializer (workaround)
            optionalProduct = Optional.ofNullable(sqlGetProductByID(id));
            return optionalProduct;


            // SQL Select all Method:
//            sqlQuery();
//            Optional<Product> optionalProduct = Optional.empty(); // Redundant initializer (workaround)
//            for (Product product:products) {
//                if(product.getId().equals(id)){
//                    optionalProduct = Optional.of(product);
//                    return optionalProduct;
//                }
//            }
//            return null;
        } catch (Exception err) {
            logEvents(
                    this.getClass().getName(),
                    new Throwable().getStackTrace()[0].getMethodName(),
                    err
            );
        }
        return null;
    }

    public void logEvents(String className, String methodName, Exception e) {
        Logger logger = LoggerFactory.getLogger(ProductService.class);
        logger.info("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }
}
