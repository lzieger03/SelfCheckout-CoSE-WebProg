package com.example.springbootapi.service;

import com.example.springbootapi.api.model.discount.Discount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the DiscountService class.
 */
public class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        String url = "jdbc:sqlite:./src/main/java/com/example/springbootapi/db/discount_database.db";
        // Remove actual connection creation, not needed for mocking
        // mockConnection = DriverManager.getConnection(url);
    }

    @Test
    void testGetDiscountByCode_Success() throws Exception {
        // Arrange
        String discountCode = "SALE15";
        Discount expectedDiscount = new Discount(discountCode, 15.0);
        when(mockStatement.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn(discountCode);
        when(mockResultSet.getDouble(2)).thenReturn(15.0);

        // Act
        Optional<Discount> result = discountService.getDiscountByCode(discountCode);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedDiscount.getCode(), result.get().getCode());
        assertEquals(expectedDiscount.getValue(), result.get().getValue());
    }

    @Test
    void testGetDiscountByCode_NotFound() throws Exception {
        // Arrange
        String discountCode = "INVALID_CODE";
        when(mockStatement.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Act
        Optional<Discount> result = discountService.getDiscountByCode(discountCode);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDiscountByCode_SQLException() throws Exception {
        // Arrange
        String discountCode = "ERROR_CODE";
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act
        Optional<Discount> result = discountService.getDiscountByCode(discountCode);

        // Assert
        assertTrue(result.isEmpty());
    }
}
