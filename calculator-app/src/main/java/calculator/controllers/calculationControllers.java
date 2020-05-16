package controllers;

import db.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;
import java.sql.*;

public class calculationControllers {

	static Connection con = dbConexion.getConnection();

	public static ModelAndView start(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/init.html");
	}

	public static ModelAndView myCalculator(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/mycalculator.html");
	}

	public static ModelAndView consult(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/consultoperation.html");
	}

	public static ModelAndView consultOperation(Request request, Response response){
		Map map = calculationControllers.checkOperation(request, response);

		if ((String)map.get("error") != null)
			return new ModelAndView(map, "./views/consultoperation.html");	
		else
			return new ModelAndView(map, "./views/showoperation.html");
	}

	public static ModelAndView calculate(Request request, Response response){
		Map map = new HashMap();
		String myExpression = (String)request.queryParams("expression");
		int save = Integer.parseInt(request.queryParams("save"));

		float result = calculationControllers.calculateExpression(myExpression);
		map.put("myexpression", myExpression);
		map.put("myresult", result);

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
						statementTwo.setString(3, Float.toString(result));
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

	private static float calculateExpression(String myExpression){
		Map map = new HashMap();

		int pos = myExpression.indexOf('+');
		if (pos != -1) {
	    	return calculateExpression(myExpression.substring(0, pos)) + calculateExpression(myExpression.substring(pos + 1));
		} else {
		    pos = myExpression.indexOf('-');
		    if (pos != -1) {
				return calculateExpression(myExpression.substring(0, pos)) - calculateExpression(myExpression.substring(pos + 1));
		    } else {
				pos = myExpression.indexOf('*');
				if (pos != -1) {
				    return calculateExpression(myExpression.substring(0, pos)) * calculateExpression(myExpression.substring(pos + 1));
				} else {
				    pos = myExpression.indexOf('/');
				    if (pos != -1) 
						return calculateExpression(myExpression.substring(0, pos)) / calculateExpression(myExpression.substring(pos + 1));
				}
			}
		}

		String toProcess = myExpression.trim();
		if( toProcess == null || toProcess.isEmpty() )
	    	return 0;
		    
		return Float.parseFloat(toProcess);
	}

	/*private static Boolean containsParent(String myExpression){
		if (myExpression.indexOf('(') != -1) return true;
		return false;
	}*/

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