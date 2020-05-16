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
	* Description: Method which converts an infix to a postfix expression
	* @return: String
	*/
	public static String aPostfija(String infija) {
		Stack<String> stack = new Stack<String>();
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
					while (!stack.empty() && isOperator(stack.peek())) 
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
	* Description: Method which evaluates a postfix expression
	* @return: Double
	*/
	public static Double evaluatePosfija(String postfija) {
		Stack<Double> stack = new Stack<Double>();

		postfija = postfija.trim();
		String[] bagExpression = postfija.split(" ");

		for(String subExpression: bagExpression) {
			try {
				double value = Double.parseDouble(subExpression);
				stack.push(value);
			} catch(NumberFormatException ex) {
				double secondOperator = stack.pop();
				double firstOperator = stack.pop();
				switch(subExpression) {       
					case "+"    :   stack.push(firstOperator+secondOperator); break;
					case "-"    :   stack.push(firstOperator-secondOperator); break;
					case "*"    :   stack.push(firstOperator*secondOperator); break;
					case "/"    :   stack.push(firstOperator/secondOperator); break;
				} 
			}
		}
		return stack.pop();
	}
}