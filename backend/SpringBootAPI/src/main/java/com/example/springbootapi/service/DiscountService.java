package com.example.springbootapi.service;


import com.example.springbootapi.api.model.Discount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Optional;

@Service
public class DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    private Discount sqlGetDiscountByCode(String id) {
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/discount_database.db";
        String query = "SELECT * FROM discounts WHERE code = ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Discount(
                            resultSet.getString(1),
                            resultSet.getDouble(2)
                    );
                }
            }
        } catch (SQLException err) {
            logEvents(this.getClass().getName(), new Throwable().getStackTrace()[0].getMethodName(), err);
        }
        return null;
    }

    public Optional<Discount> getDiscountByCode(String code) {
        return Optional.ofNullable(sqlGetDiscountByCode(code));
    }


    public void logEvents(String className, String methodName, Exception e) {
        logger.error("Error occurred in Class {} in Method {}: {}",
                className,
                methodName,
                e.getMessage()
        );
    }


}
