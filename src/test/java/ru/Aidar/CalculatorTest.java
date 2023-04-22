package ru.Aidar;

import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @org.junit.jupiter.api.Test
    void calculateWithoutBrackets() {
        assertAll(
                () -> assertEquals(-8.1, Calculator.calculate("-10.5 + 2.4")),
                () -> assertEquals(-1.1, Calculator.calculate("1 - 2.1")),
                () -> assertEquals(24.3, Calculator.calculate("10.0 * 2.43")),
                () -> assertEquals(2.5, Calculator.calculate("10.0 /   4"))
        );
    }

    @org.junit.jupiter.api.Test
    void calculateWithBrackets() {
        assertAll(
               () -> assertEquals(-8.1, Calculator.calculate("((-10.5) + 2.4)")),
                () -> assertEquals(-1.1, Calculator.calculate("(((1))) - 2.1")),
                () -> assertEquals(31.53, Calculator.calculate("((10.51 * 2)*((1+5)/2))/2")),
                () -> assertEquals(NaN, Calculator.calculate("0/0"))
        );
    }

    @org.junit.jupiter.api.Test
    void calculateThrowsException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, ()-> Calculator.calculate(")))-((10.51 * 2)*((1+5)/2))/2")),
                () -> assertThrows(IllegalArgumentException.class, ()-> Calculator.calculate("5...")),
                () -> assertThrows(IllegalArgumentException.class, ()-> Calculator.calculate("...5..."))
        );
    }
}