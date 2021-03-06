/**
 * Title: App
 * Description: Class that contains everything related to parsing and evaluating an expression
 * @author: Agustin Juarez
*/

package calculator;

import controllers.*;
import static spark.Spark.*;
import spark.template.mustache.MustacheTemplateEngine;

public class App {
    public static void main( String[] args ) {

    	staticFiles.location("/public");

		get("/init", calculationControllers::start, new MustacheTemplateEngine());

		get("/redirectsMyCalculator", calculationControllers::redirectsMyCalculator, new MustacheTemplateEngine());

		get("/redirectsConsultOperation", calculationControllers::redirectsConsultOperation, new MustacheTemplateEngine());

		get("/consultOperation", calculationControllers::consultOperation, new MustacheTemplateEngine());

		post("/calculateOperation", calculationControllers::calculateOperation, new MustacheTemplateEngine());
    }
}
