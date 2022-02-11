import { HttpClient, JsonpClientBackend } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { Recipe } from 'src/app/models/recipe.model';

@Component({
  selector: 'app-recipe-add',
  templateUrl: './recipe-add.component.html',
  styleUrls: ['./recipe-add.component.css']
})
export class RecipeAddComponent implements OnInit {


  form! : FormGroup;
  formArray!: FormArray;

  constructor(private fb: FormBuilder,
      private router: Router, private http: HttpClient) { }

  // // Initialising parameters type for the form
  // title!: string;
  // ingredientsArray!: string[];
  // instruction!: string[]
  // image!: string;
  // ingredient!: string;



  ngOnInit(): void {
    // on initialisation, populate an empty ingredient array
    this.formArray = this.fb.array([],[Validators.required]);
    this.form = this.fb.group({
      title: this.fb.control('', [Validators.required, Validators.minLength(3)]),
      image: this.fb.control('', [Validators.required]),
      instruction: this.fb.control('',[Validators.required, Validators.minLength(3)]),
      ingredients:  this.formArray
    })
  }

  addIngredient(){
    console.info('Adding an ingredient! ')
    const i = this.fb.group({
      // I Don't know why I can't validate the ingredients with required and minLength
      ingredient: this.fb.control('', [Validators.required, Validators.minLength(3)])
    })
    this.formArray.push(i);
  }

  deleteIngredient(j:number){
    console.info('Deleting an ingredient! ')
    this.formArray.removeAt(j)
  }

  onFormValid() {
    if (this.form.valid) {
      console.info("The form is valid")
      return true;
    } else {
      console.info("The form is invalid")
      return false;
    }
  }

  async addRecipe(){
    // This method is called when the recipe is added hence it will send all information to Springboot
    const data = this.form.value as Partial<Recipe>
    console.info(data);
    console.info("adding recipe");
    const send2Server = await lastValueFrom(this.http.post<any>('/api/recipe', JSON.stringify(data), {headers:{'Content-Type':'application/json','Accept':'application/json'}}));
    console.log(send2Server);
    alert('Your recipe has been sent to the server!')
    this.form.reset();
    this.router.navigate(['/']);

  }
}
