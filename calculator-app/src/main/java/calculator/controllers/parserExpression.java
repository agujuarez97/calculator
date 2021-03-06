/**
 * Title: parserExpression
 * Description: Class that contains everything related to parsing and evaluating an expression
 * @author: Agustin Juarez
*/

package controllers;

import java.util.*;

public class parserExpression {

	/**
	* Description: Auxiliary method which checks if token is +, -, *, /
	* @return: Boolean
	*/
	private static Boolean isOperator(String token) {
		if (token.compareTo("+") == 0 || token.compareTo("-") == 0 || token.compareTo("*") == 0 || token.compareTo("/") == 0)
			return true;
		return false;
	}

	/**
	* Description: Auxiliary method which checks if the expression is balanced with respect to its parentheses
	* @return: Boolean
	*/
	public static Boolean balancedExpression(String[] bagExpression) {
		Stack<String> stack = new Stack<String>();

		for (String subExpression: bagExpression) {
			if (subExpression.compareTo("(") == 0)
				stack.push(subExpression);
			if (subExpression.compareTo(")") == 0) {
				if (!stack.empty()) {
					if (stack.peek().compareTo("(") == 0)
						stack.pop();
				} else 
					return false;
			}
		}

		if (!stack.empty())
			return false;
		return true;
	}

	/**
	* Description: Method which converts an infix to a postfix expression
	* @return: String
	*/
	public static String aPostfija(String infija) {
		Stack<String> stack = new Stack<String>();

		//Initialize operator precedence level. Multiplication and division higher precedence level than addition and subtraction
		HashMap<String, Integer> precedenceOperators  = new HashMap<String, Integer>();
		precedenceOperators.put("*", 1);
		precedenceOperators.put("/", 1);
		precedenceOperators.put("+", 0);
		precedenceOperators.put("-", 0);

		String[] bagExpression = infija.split("(?=[-+*/()])|(?<=[-+*/()])");

		String subExpressionCurrent;
		String postfija = "";

		for (String subExpression: bagExpression) {
			subExpressionCurrent = subExpression;

			if (subExpressionCurrent.compareTo("(") == 0)
				stack.push(subExpressionCurrent);
			else if (subExpressionCurrent.compareTo(")") == 0) {
				while (stack.peek().compareTo("(") != 0) 
					postfija += (stack.pop() + " ");
				stack.pop();
			}
			else if (isOperator(subExpressionCurrent)) {
				if (!stack.empty()) {
					while (!stack.empty() && isOperator(stack.peek()) && precedenceOperators.get(subExpressionCurrent) <= precedenceOperators.get(stack.peek())) 
						postfija += (stack.pop() + " ");
				}
				stack.push(subExpressionCurrent);
			}
			else
				postfija += (subExpressionCurrent + " ");
		}

		while(!stack.empty())
			postfija += (stack.pop() + " ");

	   	return postfija;
	}

	/**
	* Description: Method which evaluates a postfix expression. 
	* @return: Double
	*/
	public static Double evaluatePostfija(String postfija) {
		Stack<Double> stack = new Stack<Double>();

		postfija = postfija.trim();
		String[] bagExpression = postfija.split(" ");

		for(String subExpression: bagExpression) {
			try {
				double value = Double.parseDouble(subExpression);
				stack.push(value);
			} catch(NumberFormatException ex) {
				double secondOperator = stack.pop();
				try {
					double firstOperator = stack.pop();
					try {
						switch(subExpression) {       
							case "+"    :   stack.push(firstOperator+secondOperator); break;
							case "-"    :   stack.push(firstOperator-secondOperator); break;
							case "*"    :   stack.push(firstOperator*secondOperator); break;
							case "/"    :   stack.push(firstOperator/secondOperator); break;
						}
					} catch(EmptyStackException exSecond) {
						return null;
					}
				} catch(EmptyStackException exFirst) {
					return null;
				}
			}
		}
		return stack.pop();
	}
}