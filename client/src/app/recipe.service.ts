import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { Recipe } from "./models/recipe.model";
import { Recipes } from "./models/recipes.model";

@Injectable()
export class RecipeService{
  // Task 3 -  RecipeService will be responsible for all HTTP communications between frontend and backend.

  constructor(private http: HttpClient) {}

  // Task 4 - getAllRecipes()
  public getAllRecipes(): Promise<Recipes[]>{
    // This method returns all recipes stored in the backend as an array Recipe[] in which each Recipe only contains name and RecipeId
    // in order to omit localhost:8080 - have to set up a proxy.conf.js file and serve with ng serve --proxy-config proxy.conf.js
    // specify the type Recipe[] will allow Angular to automatically convert the get response to type Recipe[] as it is able to read JSON directly.
    return lastValueFrom(this.http.get<Recipes[]>('/api/recipes'))
  }

  // Task 5 - getRecipe(recipeId)
  public getRecipe(recipeId:string){
    // Method returns specified recipe given by the recipeId parameter, it is retrieved from the backend !
    // Note that as recipeId is a dynamic route parameter, it has to be obtained using ActivatedRoute accordingly!
    return lastValueFrom(this.http.get<Recipe>(`/api/recipe/${recipeId}`))

  }

}
