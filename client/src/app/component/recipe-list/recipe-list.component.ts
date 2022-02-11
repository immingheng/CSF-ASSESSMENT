import { Component, OnInit } from '@angular/core';
import { Recipes } from 'src/app/models/recipes.model';
import { RecipeService } from 'src/app/recipe.service';

@Component({
  selector: 'app-recipe-list',
  templateUrl: './recipe-list.component.html',
  styleUrls: ['./recipe-list.component.css']
})
export class RecipeListComponent implements OnInit {

  recipes!:Recipes[];

  constructor(private recipeSvc: RecipeService) { }

  ngOnInit(): void {
    // On initialisation, recipes should be populated by a GET response from the database (Springboot) to obtain the Recipe[] to populate the list in the homepage.
    this.recipeSvc.getAllRecipes().then(resolve=>{this.recipes=resolve})

  }

}
