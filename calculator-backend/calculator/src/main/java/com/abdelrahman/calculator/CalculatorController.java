package com.abdelrahman.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class CalculatorController {
    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("{firstOperand}/{operation}/{secondOperand}")
    public String doubleOperation(@PathVariable("firstOperand") double firstOperand,
            @PathVariable("operation") String operation,
            @PathVariable("secondOperand") double secondOperand) {
        return calculatorService.doubleOperation(operation, firstOperand, secondOperand);
    }

    @GetMapping("{operation}/{operand}")
    public String singleOperation(@PathVariable("operation") String operation,
            @PathVariable("operand") double operand) {
        return calculatorService.singleOperation(operation, operand);
    }
}
