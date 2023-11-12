package com.abdelrahman.calculator;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    public String doubleOperation(String operation, double firstOperand, double secondOperand) {
        double result = 0.0;
        switch (operation) {
            case "add":
                result = firstOperand + secondOperand;
                break;
            case "subtract":
                result = firstOperand - secondOperand;
                break;
            case "multiply":
                result = firstOperand * secondOperand;
                break;
            case "divide":
                if (secondOperand == 0) {
                    return "E";
                } else {
                    result = firstOperand / secondOperand;
                }
                break;
        }
        if (result == (long) result) {
            return String.format("%d", (long) result);
        }
        return String.valueOf(result);
    }

    public String singleOperation(String operation, double operand) {
        double result = 0.0;
        switch (operation) {
            case "square":
                result = operand * operand;
                break;
            case "squareRoot":
                if (operand < 0) {
                    return "E";
                } else {
                    result = Math.sqrt(operand);
                }
                break;
            case "fraction":
                if (operand == 0) {
                    return "E";
                } else {
                    result = 1.0 / operand;
                }
                break;
            case "negate":
                result = -operand;
                break;
            case "percent":
                result = operand / 100.0;
                break;
        }
        if (result == (long) result) {
            return String.format("%d", (long) result);
        }
        return String.valueOf(result);
    }
}
