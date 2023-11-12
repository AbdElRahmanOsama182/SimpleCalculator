# Web-Based Calculator


## Overview

This project presents a web-based calculator, inspired by the Windows calculator. The application features server-side logic built with Spring Boot on the backend and a Vue.js frontend.

![Calculator.png](./calculator-frontend/src/assets/Calculator.png)
---

## **Features**

- **Basic Arithmetic Operations:** Addition, subtraction, multiplication, and division.
- **Additional Operations:** Percentage, fraction, square, square root, and negation.
- **Exception Handling:** Robust error handling for scenarios like division by zero, displaying 'E' as an indicator.
- **User-Friendly Interface:** Web buttons for intuitive user interaction.

---

## **Technologies Used**

- **Vue.js:** The frontend leverages Vue.js to provide a dynamic and responsive user interface.
- **Spring Boot:** The backend is developed with Spring Boot, a Java-based framework, handling server-side logic and REST API endpoints.
- **Fetch API:** Enables communication between the frontend and backend for performing calculations.

---

## **Vue.js Code Overview**

### **`data()`**

The **`data()`** function initializes the component's data properties, including expressions, current numbers, and various flags used during calculations.

```jsx
data() {
    return {
        expression: '',
        currNum: '',
        firstOperand: '',
        secondOperand: '',
        opertion: '',
        operator: false,
        operatorDone: false,
        done: false,
        prevSingle: false,
        temp: '',
    }
}
```

### **Important Methods**

### **`clear()`**

The **`clear()`** method resets the calculator's state, clearing the expression, current number, and other relevant properties.

```jsx
clear() {
    this.expression = '';
    this.currNum = '';
    this.firstOperand = '';
    this.secondOperand = '';
    this.opertion = '';
    this.operator = false;
    this.operatorDone = false;
    this.prevSingle = false;
    this.done = false;
}
```

### **`deleting()`**

The **`deleting()`** method handles the backspace functionality, allowing users to delete the last digit or operator.

```jsx
deleting() {
    if(this.currNum==='E' || this.done){
        this.clear();
    } 
    if ((this.currNum === '' && this.operator)||this.operatorDone) {
        this.temp = this.firstOperand;
        this.clear();
        this.currNum = this.temp;
        
    }
    else this.currNum = this.currNum.slice(0, -1);
}
```

### **`appendNum(num)`**

The **`appendNum(num)`** method handles the input of numeric values, updating the current number displayed on the calculator.

```jsx
appendNum(num) {
    if(this.currNum==='E') this.clear();
    if(this.done||this.prevSingle) {
        this.clear();
    }
    if(this.operatorDone) {
        this.firstOperand = this.currNum;
        this.currNum = '';
        this.operatorDone = false;
    }
    if (num === '.' && this.currNum.includes('.')) return;
    if (this.currNum === '0') this.deleting();
    if (num === '.' && this.currNum === '') this.currNum = '0';
    this.currNum += num;
}
```

### **`singleOperation(op)`**

The **`singleOperation(op)`** method handles unary operations like square, square root, and negation.

```jsx
async singleOperation(op) {
    if (this.currNum !== '' && this.currNum !== 'E' && !this.operatorDone) {
        await fetch(`http://localhost:8081/`+op+`/`+this.currNum, {
            method: 'get',
        }).then(res => {
            return res.text();
        }).then(data => {
            this.currNum = data;
        });
        if (this.done) {
            this.temp = this.currNum;
            this.clear();
            this.currNum = this.temp;
        }
        this.prevSingle = true;
    }
}
```

### **`doubleOperation(op, opSymbol)`**

The **`doubleOperation(op, opSymbol)`** method handles binary operations like addition, subtraction, multiplication, and division.

```jsx
async doubleOperation(op,opSymbol) {
    if (this.currNum === '' || this.currNum === 'E') return;
    if (this.done) {
        this.temp = this.currNum;
        this.clear();
        this.currNum = this.temp;
    }
    if (!this.operator) {
        this.prevSingle = false;
        this.firstOperand = this.currNum;
        this.expression = this.expression + ' ' + this.firstOperand + ' ' + opSymbol;
        this.opertion = op;
        this.operator = true;
        this.currNum = '';
    } else if (!this.operatorDone){
        this.prevSingle = false;
        this.secondOperand = this.currNum;
        await fetch(`http://localhost:8081/`+this.firstOperand+`/`+this.opertion+`/`+this.secondOperand, {
            method: 'get',
        }).then(res => {
            return res.text();
        }).then(data => {
            this.currNum = data;
        });
        if (this.currNum !== 'E') {
            this.expression = this.expression + ' ' + this.secondOperand + ' ' + opSymbol;
        }
        else {
            this.expression = this.expression + ' ' + this.secondOperand;
        }
        this.firstOperand = this.currNum;
        this.opertion = op;
        this.operator = true;
        this.operatorDone = true;
    }
}
```

### **`equal()`**

The **`equal()`** method calculates the result when the equal button is pressed.

```jsx
equal(){
    if (!this.done && this.currNum !== '' && this.currNum !== 'E' && !this.operatorDone) {
        if (this.operator) {
            this.secondOperand = this.currNum;
            this.expression = this.expression + ' ' + this.secondOperand;
            fetch(`http://localhost:8081/`+this.firstOperand+`/`+this.opertion+`/`+this.secondOperand, {
                method: 'get',
            }).then(res => {
                return res.text();
            }).then(data => {
                this.currNum = data;
            });
        }
        this.done = true;
    }
}
```

### **Frontend Structure**

The Vue.js frontend is organized with components and styles, providing a clean and modular structure for the calculator interface.

- **Components:** The **`Calculator.vue`** component encapsulates the main logic and UI for the calculator.
- **Styles:** Styles for the calculator are defined within the component's **`<style>`** section.

---

## **Spring Boot Code Overview**

### **`CalculatorService.java`**

### **`doubleOperation(...)`**

The **`doubleOperation(...)`** method in the **`CalculatorService`** class performs the actual arithmetic calculations for double-operand operations.

```java
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
```

### **`singleOperation(...)`**

The **`singleOperation(...)`** method in the **`CalculatorService`** class performs the actual calculations for single-operand operations.

```java
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
```

## **Backend Structure**

The Spring Boot backend follows a modular structure:

- **Application Class:** The **`CalculatorApplication.java`** class serves as the entry point for the Spring Boot application.
- **Controller:** The **`CalculatorController.java`** class defines REST endpoints for calculator operations.
- **Service:** The **`CalculatorService.java`** class implements the calculator's logic.

> The backend server will be available at **`http://localhost:8081/`**.
> 

---

## **API Endpoints**

### **Double Operation (Arithmetic)**

Perform double-operand arithmetic operations (add, subtract, multiply, divide).

**Endpoint:** **`/doubleOperation/{firstOperand}/{operation}/{secondOperand}`**

```java
@GetMapping("{firstOperand}/{operation}/{secondOperand}")
public String doubleOperation(@PathVariable("firstOperand") double firstOperand,
        @PathVariable("operation") String operation,
        @PathVariable("secondOperand") double secondOperand) {
    return calculatorService.doubleOperation(operation, firstOperand, secondOperand);
}
```

### **Single Operation (Unary)**

Perform single-operand operations (square, square root, fraction, negate, percentage).

**Endpoint:** **`/singleOperation/{operation}/{operand}`**

```java
@GetMapping("{operation}/{operand}")
public String singleOperation(@PathVariable("operation") String operation,
        @PathVariable("operand") double operand) {
    return calculatorService.singleOperation(operation, operand);
}
```

---
