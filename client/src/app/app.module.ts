import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { RecipeListComponent } from './component/recipe-list/recipe-list.component';
import { RecipeDetailComponent } from './component/recipe-detail/recipe-detail.component';
import { RecipeAddComponent } from './component/recipe-add/recipe-add.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

const appRoutes : Routes = [
  {path: '', component: RecipeListComponent},
  {path:'recipe/:recipeId', component: RecipeDetailComponent},
  {path:'add', component: RecipeAddComponent},
  {path:'**', redirectTo:'/', pathMatch:'full'}
]

@NgModule({
  declarations: [
    AppComponent,
    RecipeListComponent,
    RecipeDetailComponent,
    RecipeAddComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,ReactiveFormsModule,
    RouterModule.forRoot(appRoutes,{useHash:true})
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }