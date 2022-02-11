package ibf2021.assessment.csf.server.controllers;

import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* Write your request hander in this file */

@RestController
@RequestMapping(path = "/api/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeRestController {

    private final Logger logger = Logger.getLogger(RecipeRestController.class.getName());

    // Task 5 Springboot side
    // Handler to handle request from view 2 and should use RecipeService to get the
    // recipe using the provided recipe ID
    @GetMapping
    public ResponseEntity<String> getRecipe(@PathVariable(name = "id") String id) {
        logger.info("Path Variable -->"+ id);
        return null;
    }

}
