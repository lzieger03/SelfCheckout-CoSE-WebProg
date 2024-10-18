package com.example.springbootapi.api.controller;

import com.example.springbootapi.api.model.discount.Discount;
import com.example.springbootapi.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the DiscountController class.
 */
public class DiscountControllerTest {

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private DiscountController discountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDiscount_Success() {
        // Arrange
        Discount discount = new Discount("CODE123", 20.0);
        when(discountService.getDiscountByCode(anyString())).thenReturn(Optional.of(discount));

        // Act
        ResponseEntity<Discount> response = discountController.getDiscount("CODE123");

        // Assert
        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        assertEquals(discount, response.getBody());
    }

    @Test
    void testGetDiscount_NotFound() {
        // Arrange
        when(discountService.getDiscountByCode(anyString())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Discount> response = discountController.getDiscount("INVALID_CODE");

        // Assert
        assertEquals(HttpStatus.valueOf(404), response.getStatusCode());
    }

    @Test
    void testGetDiscount_InternalServerError() {
        // Arrange
        when(discountService.getDiscountByCode(anyString())).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Discount> response = discountController.getDiscount("ERROR_CODE");

        // Assert
        assertEquals(HttpStatus.valueOf(500), response.getStatusCode());
        assertEquals("Database error", Objects.requireNonNull(response.getBody()).getCode());
        assertEquals(0.0, response.getBody().getValue());
    }
}
