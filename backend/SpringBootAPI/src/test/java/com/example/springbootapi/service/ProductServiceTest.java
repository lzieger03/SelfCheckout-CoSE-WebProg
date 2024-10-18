package com.example.springbootapi.service;

import com.example.springbootapi.api.model.product.Product;
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
 * Unit tests for the ProductService class.
 */
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductByID_Success() throws Exception {
        // Arrange
        String productId = "8156679408476";
        Product expectedProduct = new Product(productId, "Apple", 0.99);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(1)).thenReturn(productId);
        when(mockResultSet.getString(2)).thenReturn("Apple");
        when(mockResultSet.getDouble(3)).thenReturn(0.99);

        // Act
        Optional<Product> result = productService.getProduct(productId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedProduct.getId(), result.get().getId());
        assertEquals(expectedProduct.getName(), result.get().getName());
        assertEquals(expectedProduct.getPrice(), result.get().getPrice());
    }

    @Test
    void testGetProductByID_NotFound() throws Exception {
        // Arrange
        String productId = "INVALID_ID";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        // Act
        Optional<Product> result = productService.getProduct(productId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProductByID_SQLException() throws Exception {
        // Arrange
        String productId = "ERROR_ID";
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act
        Optional<Product> result = productService.getProduct(productId);

        // Assert
        assertTrue(result.isEmpty());
    }
}
