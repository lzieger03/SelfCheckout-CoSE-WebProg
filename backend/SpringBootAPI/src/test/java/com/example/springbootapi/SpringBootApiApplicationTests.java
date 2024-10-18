package com.example.springbootapi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integrationstest für die SpringBootApiApplication.
 */
@SpringBootTest
class SpringBootApiApplicationTests {

    @Test
    @DisplayName("Context Loads Successfully")
    void contextLoads() {
        // Dieser Test überprüft, ob der Anwendungskontext ohne Fehler geladen wird
    }

    // Weitere Integrationstests können hier hinzugefügt werden
}
