package com.example.springbootapi.bonprintextended;

/**
 * Interface representing a component that can be added to a POSDocument.
 * Each component must be convertible to a byte array for printing.
 */
public interface POSComponent {
    /**
     * Converts the component into a byte array suitable for ESC/POS printing.
     *
     * @return A byte array representing the component.
     */
    byte[] toBytes();
}