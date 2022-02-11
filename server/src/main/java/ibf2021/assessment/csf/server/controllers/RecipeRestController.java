package ibf2021.assessment.csf.server.controllers;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2021.assessment.csf.server.models.Recipe;
import ibf2021.assessment.csf.server.services.RecipeService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

/* Write your request hander in this file */

@RestController
@RequestMapping(path = "/api/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeRestController {

    private final Logger logger = Logger.getLogger(RecipeRestController.class.getName());

    @Autowired
    private RecipeService recipeSvc;

    // Task 5 Springboot side
    // Handler to handle request from view 2 and should use RecipeService to get the
    // recipe using the provided recipe ID
    @GetMapping(path = "{id}")
    public ResponseEntity<String> getRecipe(@PathVariable String id) {
        logger.info("Path Variable -->" + id);
        Optional<Recipe> r = this.recipeSvc.getRecipeById(id);
        // as r can be of nullable or present, handle them accordingly
        if (r.isPresent()) {
            // now r is of type Recipe, have to convert it to JSON and pass it back to
            // frontend
            // As ingredients is in a List, have to add individual ingredient into a JSON
            // Array to be passed into the JSON that will be passed on to the client.

            JsonObjectBuilder joBuilder = Json.createObjectBuilder()
                    .add("recipeId", r.get().getId())
                    .add("name", r.get().getTitle())
                    .add("image", r.get().getImage())
                    .add("instruction", r.get().getInstruction());

            JsonArrayBuilder jarrayBuilder = Json.createArrayBuilder();
            for (String ingredient : r.get().getIngredients()) {
                jarrayBuilder.add(ingredient);
            }
            JsonArray jarray = jarrayBuilder.build();

            JsonObject jo = joBuilder.add("ingredients", jarray)
                    .build();

            return ResponseEntity.ok().body(jo.toString());

        } else {
            // if r does not exists, it means that the recipe id is non-existantial
            // therefore have to return a 404 status code and an error object of choice with
            // error details
            JsonObject joError = Json.createObjectBuilder()
                    .add("recipeId", "NOT FOUND")
                    .add("name", "NOT FOUND")
                    .add("image", "NOT FOUND")
                    .add("instruction", "NOT FOUND")
                    .add("ingredients", "NOT FOUND")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(joError.toString());

        }

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> recipeFromClient(@RequestBody String payload) throws IOException {
        logger.info("Recipe payload -->" + payload);

        // within the model, when a new recipe is created, an instance of its id is
        // already set therefore only have to manipulate the payload to the form
        // required to set other properties of the recipe prior to calling the service's
        // method addRecipe(Recipe)

        // payload is in the form of a JSON therefore have to extract values
        // accordingly
        // Parsing String to JSON
        try (Reader reader = new StringReader(payload)) {
            JsonReader JSONreader = Json.createReader(reader);
            JsonObject recipe = JSONreader.readObject();

            Recipe saveRecipe = new Recipe();

            saveRecipe.setImage(recipe.getString("image"));

            saveRecipe.setTitle(recipe.getString("title"));

            saveRecipe.setInstruction(recipe.getString("instruction"));

            recipe.getJsonArray("ingredients").stream()
                    .forEach(ingredient -> saveRecipe.addIngredient(ingredient.toString()));

            logger.info("recipe to save --> " + saveRecipe.toString());

            logger.info("ingredient exist -->" + saveRecipe.getIngredients().get(0));

            JsonObject messageToReturn = Json.createObjectBuilder()
                    .add("message", "Your recipe has been added to the server!").build();

            this.recipeSvc.addRecipe(saveRecipe);

            return ResponseEntity.status(HttpStatus.CREATED).body(messageToReturn.toString());

        }
        // assuming no errors ,don't have to handle bad request

    }

}
