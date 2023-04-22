package ru.Aidar;

import java.util.*;
import java.util.regex.Pattern;

public class Calculator {
    /*
    Задание: Необходимо написать калькулятор, принимающий  строку с выражением, включающим ‘+’, ’-‘, ’* ’, ’/’.
    Предусмотреть использование скобок.
    Результат выражения выводить в консоль.
    Покрыть написанный код юнит тестами.
    Необходимо использовать java 11.

    Формат ввода задан не точно. Поэтому:
    1. разделитель - точка
    2. на переполнение не смотрю. Если переполнение, то надо будет использовать другие классы. Думаю, тут главное алгоритм
    Тут есть большое использование памяти из-за работы со строками. Да, это можно переписать на массивах char. Будет экономичнее по памяти и времени
    В консоль выводяться все вычисления по порядку. конечное вычисление содержит ответ
     */
    private static final Map<Character, Integer> operatorsWithPriority;
    private final static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    static {
        operatorsWithPriority = new HashMap<>();
        operatorsWithPriority.put('(', 0);
        operatorsWithPriority.put('+', 1);
        operatorsWithPriority.put('-', 1);
        operatorsWithPriority.put('*', 2);
        operatorsWithPriority.put('/', 2);
    }

    public static double calculate(String input) {
        int counter = 0;
        double firstNumber;
        double secondNumber;
        input = input.replaceAll("\\s+", "");
        ArrayList<String> postfixNotation;
        try {
            postfixNotation = getPostfixNotation(input);
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException(" Wrong input, check it again, pay attention on ')'");
        }
        Stack<Double> numberStack = new Stack<>();
        for (String str : postfixNotation) {
            if (isNumeric(str)) {
                numberStack.add(Double.parseDouble(str));
            } else if (str.length() == 1 && operatorsWithPriority.containsKey(str.charAt(0))) {
                counter++;
                secondNumber = numberStack.isEmpty() ? 0 : numberStack.pop();
                firstNumber = numberStack.isEmpty() ? 0 : numberStack.pop();
                numberStack.push(executeSimpleOperation(firstNumber, secondNumber, str.charAt(0)));
                System.out.println("# " + counter + " " + " " + firstNumber + " " + str + " " + secondNumber + "=" + numberStack.peek());
            }
        }
        if(numberStack.isEmpty()){
            throw new IllegalArgumentException(" Wrong input, check it again : " + input);
        }
        return numberStack.pop();
    }

    private static double executeSimpleOperation(double first, double second, char operator) {
        double res;
        switch (operator) {
            case '+':
                res = first + second;
                break;
            case '-':
                res = first - second;
                break;
            case '*':
                res = first * second;
                break;
            case '/':
                res = first / second;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported operator: " + operator);
        }
        return res;
    }

    private static ArrayList<String> getPostfixNotation(String input) {
        int length = input.length();
        ArrayList<String> postfixNotation = new ArrayList<>();
        Stack<Character> operatorStack = new Stack<>();
        for (int i = 0; i < length; i++) {
            char curChar = input.charAt(i);
            if (Character.isDigit(curChar) || curChar == '.') { //add number
                int startPosition = i;
                while (i < length && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    if(i!=startPosition && input.charAt(i) == '.' && input.charAt(i-1) == '.'){
                        throw new IllegalArgumentException("Find multiply following dots");
                    }
                    i++;
                }
                postfixNotation.add(input.substring(startPosition, i));
                i--;
            } else if (curChar == '(') {
                operatorStack.add(curChar);
            } else if (curChar == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') { //add all operators till the "("
                    postfixNotation.add(String.valueOf(operatorStack.pop()));
                }
                operatorStack.pop();// remove '('
            } else if (operatorsWithPriority.containsKey(curChar)) {// check if allowed operator
                while (!operatorStack.isEmpty() && (operatorsWithPriority.get(operatorStack.peek()) >= operatorsWithPriority.get(curChar))) // get operators with high priority
                {
                    postfixNotation.add(String.valueOf(operatorStack.pop()));
                }
                operatorStack.push(curChar);
            } else {
                throw new IllegalArgumentException("Wrong char in input line " + curChar);
            }
        }
        while (!operatorStack.isEmpty()) {
            postfixNotation.add(String.valueOf(operatorStack.pop()));
        }
        return postfixNotation;
    }


    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}