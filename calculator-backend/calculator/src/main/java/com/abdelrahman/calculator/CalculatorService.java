package com.abdelrahman.calculator;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    ExpressionParser parser = new SpelExpressionParser();

    public String expressionSolver(String expression) {
        try {
            expression = expression.replaceAll("÷", "* 1.0 /").replaceAll("×", "*").replaceAll("−", "-");
            double result = parser.parseExpression(expression).getValue(Double.class);
            if (result == (long) result) {
                return String.format("%d", (long) result);
            }
            return String.valueOf(result);
        } catch (Exception e) {
            return "E";
        }
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
