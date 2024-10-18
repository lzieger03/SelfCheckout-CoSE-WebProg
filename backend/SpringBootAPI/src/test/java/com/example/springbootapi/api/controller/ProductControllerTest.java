package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.product.Product;
import com.example.springbootapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the ProductController class.
 */
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProduct_Success() {
        // Arrange
        Product product = new Product("1", "Product Name", 100.0);
        when(productService.getProduct(anyString())).thenReturn(Optional.of(product));

        // Act
        ResponseEntity<Product> response = productController.getProduct("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
    }

    @Test
    void testGetProduct_NotFound() {
        // Arrange
        when(productService.getProduct(anyString())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Product> response = productController.getProduct("INVALID_ID");

        // Assert
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetProduct_InternalServerError() {
        // Arrange
        when(productService.getProduct(anyString())).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Product> response = productController.getProduct("ERROR_ID");

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Database error", response.getBody().getName());
        assertEquals(0.0, response.getBody().getPrice());
    }
}
