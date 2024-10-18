package com.example.springbootapi.api.exception;

import com.example.springbootapi.api.controller.ProductController;
import com.example.springbootapi.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integrationstest für den GlobalExceptionHandler.
 */
@WebMvcTest(controllers = ProductController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Handle general exceptions and return 500 Internal Server Error")
    void testHandleAllExceptions() throws Exception {
        // Arrange
        String productId = "error";
        when(productService.getProduct(productId)).thenThrow(new RuntimeException("Allgemeiner Fehler"));

        // Act & Assert
        mockMvc.perform(get("/product")
                .param("id", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    @DisplayName("Handle validation exceptions and return 400 Bad Request")
    void testHandleValidationExceptions() throws Exception {
        // Da ProductController keine Validierungen auf GET-Anfragen hat,
        // simulieren wir eine POST-Anfrage, die Validierungsfehler verursachen würde.

        // Beispielhafte Annahme einer POST-Anfrage zum Drucken eines Belegs
        String invalidRequestBody = "{}"; // Leerer Body verursacht Validierungsfehler

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/print")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.paymentMethod").exists()); // Passen Sie die Felder entsprechend an
    }
}
