package ibf2021.assessment.csf.server.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import ibf2021.assessment.csf.server.models.Recipe;

public class RecipeLoader {

	public static final String TITLE = "title:";
	public static final String IMAGE = "image:";
	public static final String INGREDIENT = "ingredient:";
	public static final String INSTRUCTION = "instruction:";
	public static final String ID = "id:";

	public static List<Recipe> loadRecipes(InputStream is) throws Exception {

		List<Recipe> recipes = new LinkedList<>();

		try (InputStreamReader isr = new InputStreamReader(is);) {
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuilder strBuilder = new StringBuilder();
			Recipe recipe = new Recipe();
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() <= 0) {
					recipe.setInstruction(strBuilder.toString());
					recipes.add(recipe);
					recipe = new Recipe();
					strBuilder = new StringBuilder();
					continue;

				} else if (line.startsWith(ID)) {
					String id = line.substring(ID.length()).trim();
					recipe.setId(id);

				} else if (line.startsWith(TITLE)) {
					String title = line.substring(TITLE.length()).trim();
					recipe.setTitle(title);

				} else if (line.startsWith(IMAGE)) {
					String image = line.substring(IMAGE.length()).trim();
					recipe.setImage(image);

				} else if (line.startsWith(INGREDIENT)) {
					String ingredient = line.substring(INGREDIENT.length()).trim();
					recipe.addIngredient(ingredient);

				} else if (line.startsWith(INSTRUCTION)) {
					String instruction = line.substring(INSTRUCTION.length()).trim();
					strBuilder = strBuilder.append("%s\n".formatted(instruction));
				}
			}
			// make sure we have save the last recipe
			// Hack: check if title is non zero
			if (recipe.getIngredients().size() > 0) {
				recipe.setInstruction(strBuilder.toString());
				recipes.add(recipe);
			}
		}

		return recipes;
	}
}
