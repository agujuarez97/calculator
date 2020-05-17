/**
 * Title: calculationControllers
 * Description: This class controls all the actions belonging to an calculator
 * @author: Agustin Juarez
*/

package controllers;

import db.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;
import java.sql.*;

public class calculationControllers {

	static Connection con = dbConexion.getConnection();

	/**
	* Description: This method redirects the init.html view
	* @return: ModelAndView
	*/
	public static ModelAndView start(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/init.html");
	}

	/**
	* Description: This method redirects the mycalculator.html view
	* @return: ModelAndView
	*/
	public static ModelAndView myCalculator(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/mycalculator.html");
	}

	/**
	* Description: This method redirects the consultoperation.html view
	* @return: ModelAndView
	*/
	public static ModelAndView consult(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/consultoperation.html");
	}

	/**
	* Description: The method queries a stored operation. If this operation does not exist, it redirects to the consulttoperation.html view showing an error message, 
	*			   otherwise it redirects to the showoperation.html view to show this operation
	* @return: ModelAndView
	*/
	public static ModelAndView consultOperation(Request request, Response response){
		Map map = calculationControllers.checkOperation(request, response);

		if ((String)map.get("error") != null)
			return new ModelAndView(map, "./views/consultoperation.html");	
		else
			return new ModelAndView(map, "./views/showoperation.html");
	}

	/**
	* Description: Method which performs the evaluation of an expression. First check that the expression is well balanced in terms of parentheses. Second, it takes the infix expression and passes it to postfix to later evaluate that expression.
	*              In addition to evaluating the expression in case you have selected to evaluate and save the expression the save
	* @return: ModelAndView
	*/
	public static ModelAndView calculate(Request request, Response response){
		Map map = new HashMap();
		String myExpression = (String)request.queryParams("expression");

		//Check that the expression is balanced in parentheses
		if (!parserExpression.balancedExpression(myExpression.split("(?=[-+*/()])|(?<=[-+*/()])"))){
			map.put("myexpression", myExpression);
			map.put("error", "<div class='alert alert-danger'><strong>Expresión invalida:</strong> Error en su sintaxis.</div>");
			return new ModelAndView(map, "./views/mycalculator.html");
		}

		String postfija = parserExpression.aPostfija(myExpression);
		Double result = parserExpression.evaluatePostfija(postfija);

		//Check that the expression is syntactically correct
		if (result == null) {
			map.put("myexpression", myExpression);
			map.put("error", "<div class='alert alert-danger'><strong>Expresión invalida:</strong> Error en su sintaxis.</div>");
			return new ModelAndView(map, "./views/mycalculator.html");
		}

		map.put("myexpression", myExpression);
		map.put("myresult", result);

		//Check if the operation wants to be saved
		int save = Integer.parseInt(request.queryParams("save"));
		if (save == 1)
			return new ModelAndView(map, "./views/mycalculator.html");
		else {
			int id = Integer.parseInt(request.queryParams("idSave"));
			try {
				String queryOne = "SELECT * FROM mycalculation WHERE id = '"+id+"'";
				PreparedStatement statementOne = con.prepareStatement(queryOne);
				ResultSet resultSet = statementOne.executeQuery();

				if (resultSet.next()) {
					map.put("error", "<div class='alert alert-danger'><strong>Error:</strong> Existe una operacion almacenada con ese ID.</div>");
					return new ModelAndView(map, "./views/mycalculator.html");
				} else {
					try {
						String queryTwo = "INSERT INTO mycalculation VALUES (?,?,?)";
						PreparedStatement statementTwo = con.prepareStatement(queryTwo);
						statementTwo.setInt(1, id);
						statementTwo.setString(2, myExpression);
						statementTwo.setString(3, Double.toString(result));
						statementTwo.executeUpdate();
					} catch (SQLException ex) {
		   				ex.printStackTrace();
					}
					map.put("saveExit", "<div class='alert alert-success'><strong>Operacion guardada correctamente, recuerde que el ID para recuperarla es: " +id+ ".</strong></div>");
					return new ModelAndView(map, "./views/mycalculator.html");
				} 
			} catch (SQLException ex) {
   				ex.printStackTrace();
			}
		}
		return new ModelAndView(map, "./views/mycalculator.html");
	}

	/**
	* Description: Method which searches for a stored expression, at home that exists, returns it but returns an error message
	* @return: ModelAndView
	*/
	private static Map checkOperation(Request request, Response response){
		int idOperation = Integer.parseInt(request.queryParams("idOperation"));
		Map mapOperations = new HashMap();
		
		try {
			String query = "SELECT * FROM mycalculation WHERE id = '"+idOperation+"'";
			PreparedStatement st = con.prepareStatement(query);
			ResultSet result = st.executeQuery();
			
			if (result.next()) {
				String expression = result.getString("expression");
				String resultExpression = result.getString("result");

				mapOperations.put("myexpression", expression);
				mapOperations.put("myresult", resultExpression);
				return mapOperations;
			} else {
				mapOperations.put("error", "<div class='alert alert-danger'><strong>Error:</strong> No existe ninguna operacion con ese ID.</div>");
				return mapOperations;
			}
				 
		 } 
 		catch (SQLException ex) {
   			ex.printStackTrace();
		}
		return mapOperations;
	}
}