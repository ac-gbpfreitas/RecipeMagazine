package com.example.pickrecipe.json

import android.content.Context
import android.util.Log
import com.example.pickrecipe.db.model.IngredientEntity
import com.example.pickrecipe.db.model.RecipeEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class RecipeJsonReaderTester (context : Context, fileName : String) {


    //Refer to Json class file and create a list
    private var myRecipeJsonType = Types.newParameterizedType(List::class.java, RecipeJson::class.java)


    //List of Recipe and Ingredient Entities - Database related
    var recipeEntities : ArrayList<RecipeEntity> = arrayListOf();

    companion object{
        var recipeInventory : ArrayList<RecipeJson> = arrayListOf();
    }

    init{

        var jsonContent = FileReaderHelper.getDataFromAssets(context,fileName);
        var moshi : Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        var adapter : JsonAdapter<List<RecipeJson>> = moshi.adapter(myRecipeJsonType);

        var recipeListJson = adapter.fromJson(jsonContent);

        //Check if the json is empty
        if(!recipeListJson.isNullOrEmpty()){



            //Loop to get every record from json
            for((i, newRecipe) in recipeListJson.withIndex()){
                var ingredients : String = "";
                //A loop to get every ingredient from the json ingredientList, from each recipe
                for((j,newIngredient) in newRecipe.ingredients.withIndex()){
                    ingredients = newIngredient.quantity.toString()+"|";
                    ingredients += newIngredient.unity+"|";
                    ingredients += newIngredient.detail+"|";
                    ingredients += "#id:"+newIngredient.ingredientId+"|\n";
                }

                //Create a recipeEntity Object
                var recipe = RecipeEntity(
                    newRecipe.id,
                    newRecipe.recipeTitle,
                    newRecipe.rating,
                    newRecipe.details,
                    newRecipe.directions,
                    newRecipe.picture,
                    ingredients
                );

                //Add the recipe object to the list
                recipeEntities.add(recipe);
            }
        }
    }
}