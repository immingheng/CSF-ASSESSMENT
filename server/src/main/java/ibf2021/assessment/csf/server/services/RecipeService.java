package ibf2021.assessment.csf.server.services;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import ibf2021.assessment.csf.server.ServerApplication;
import ibf2021.assessment.csf.server.models.Recipe;
import ibf2021.assessment.csf.server.utils.RecipeLoader;

/*
 * IMPORTANT: Do not add, remove or change any thing in this class
 */

@Service
public class RecipeService {

	public static final String SEED = "classpath:data/seed.txt";

	private final Logger logger = Logger.getLogger(ServerApplication.class.getName());

	private List<Recipe> recipes = new LinkedList<>();
	private Map<String, Recipe> recipesById = new HashMap<>();

	private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

	@Autowired
	ResourceLoader resourceLoader;

	public RecipeService() { }

	@PostConstruct
	public void init() {
		try {
			Resource resource = resourceLoader.getResource(SEED);
			final InputStream is = resource.getInputStream();
			recipes = RecipeLoader.loadRecipes(is);
			recipes.forEach(r -> recipesById.put(r.getId(), r));
			logger.info("Loaded initial recipes: %s".formatted(recipes));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Optional<Recipe> getRecipeById(final String id) {
		return readLock(() -> Optional.ofNullable(recipesById.get(id)));
	}

	public List<Recipe> getAllRecipes() {
		return readLock(() -> Collections.unmodifiableList(recipes));
	}

	public void addRecipe(final Recipe recipe) {
		writeLock(() -> {
			recipes.add(recipe);
			recipesById.put(recipe.getId(), recipe);
		});
	}

	private <T> T readLock(Supplier<T> s) {
		final Lock l = rwLock.readLock();
		l.lock();
		try {
			return s.get();
		} finally { 
			l.unlock(); }
	}

	private void writeLock(Runnable r) {
		final Lock l = rwLock.writeLock();
		l.lock();
		try {
			r.run();
		} finally { l.unlock(); }
	}
}
