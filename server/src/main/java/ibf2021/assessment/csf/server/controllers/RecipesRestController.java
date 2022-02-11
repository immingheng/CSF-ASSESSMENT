package ibf2021.assessment.csf.server.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2021.assessment.csf.server.models.Recipe;
import ibf2021.assessment.csf.server.services.RecipeService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

/* Write your request hander in this file */

@RestController
@RequestMapping(path = "/api/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipesRestController {

    private final Logger logger = Logger.getLogger(RecipesRestController.class.getName());

    List<Recipe> recipes = new LinkedList<>();

    @Autowired
    private RecipeService recipeSvc;

    // this will map to the get response from the client side to return the list of
    // Recipe
    @GetMapping
    public ResponseEntity<String> getRecipes() {
        // using the RecipeService, I am able to obtain the entire List<Recipe>
        recipes = recipeSvc.getAllRecipes();
        logger.info("Recipes --> " + recipes);
        // To pass this back to the client, the list has to be manipulated to only pass
        // id and title! This will then require JSON-P to build the JSON Object to be
        // passed!
        JsonArrayBuilder jArray = Json.createArrayBuilder();
        for (Recipe r : recipes) {
            logger.info("recipe --> " + r);
            JsonObject jo = Json.createObjectBuilder().add("recipeId", r.getId())
                    .add("name", r.getTitle())
                    .build();
            jArray.add(jo);
        }
        JsonArray resp = jArray.build();

        return ResponseEntity.ok().body(resp.toString());

    }

}