import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Recipe } from 'src/app/models/recipe.model';
import { RecipeService } from 'src/app/recipe.service';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.css']
})
export class RecipeDetailComponent implements OnInit {

  recipeId = '';
  recipe! : Recipe;

  constructor(private activatedRoute: ActivatedRoute, private recipeSvc: RecipeService) { }

  ngOnInit(): void {
    this.recipeId = this.activatedRoute.snapshot.params['recipeId']
    console.info('RECIPE ID --> '+this.recipeId);
    this.recipeSvc.getRecipe(this.recipeId).then(result => this.recipe = result)
  }

}
