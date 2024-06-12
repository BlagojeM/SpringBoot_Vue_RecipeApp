<template>
  <div>
    <UButton
      class="m-2.5"
      color="primary"
      variant="outline"
      label="Dodaj recept"
      @click="addRecipeDialog = true"
    />

    <UModal v-model="addRecipeDialog">
      <div class="p-4 flex flex-col">
        <UInput
          color="primary"
          variant="outline"
          placeholder="Naziv"
          v-model="title"
        />
        <UTextarea
          color="primary"
          variant="outline"
          placeholder="Opis"
          v-model="description"
        />
        <div class="flex flex-col items-center">
          <base64
            label="Dodaj sliku"
            ref="base64Ref33"
            :multiple="false"
            @done="getImage"
          ></base64>
          <img
            :src="
              image ||
              'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwZgTsV5FSzcygnwaRW4SePUSXSiNZCdYUhw&s'
            "
            style="
              width: 100px;
              height: 100px;
              border-radius: 5px;
              object-fit: cover;
            "
          />
        </div>
        <span> Sastojci: </span>
        <div
          v-for="(ingredient, index) in ingredients"
          :key="'Add Ingredients - ' + index"
          class="flex flex-row"
        >
          <UInput
            color="primary"
            variant="outline"
            placeholder="Naziv"
            v-model="ingredient.name"
          />
          <UInput
            color="primary"
            variant="outline"
            placeholder="Količina"
            v-model="ingredient.number"
          />
          <UInput
            color="primary"
            variant="outline"
            placeholder="Merna jedinica"
            v-model="ingredient.unit"
          />
        </div>

        <div class="flex justify-center m-2.5">
          <UButton
            icon="i-heroicons-plus"
            size="sm"
            color="primary"
            :ui="{ rounded: 'rounded-full' }"
            variant="outline"
            @click="addIngredient"
          />
        </div>
        <div class="flex justify-between">
          <UButton
            class="m-2.5"
            color="primary"
            variant="outline"
            label="Sačuvaj"
            @click="
              () => {
                id == null ? addRecipe() : editRecipePut();
              }
            "
          />
          <UButton
            class="m-2.5"
            color="primary"
            variant="outline"
            label="Odustani"
            @click="closeRecipeDialog"
          />
        </div>
      </div>
    </UModal>

    <div class="flex flex-row">
      <SingleRecipe
        v-for="(recipe, index) in recipes"
        :key="'recepie - ' + index"
        :id="recipe.id"
        :title="recipe.title"
        :description="recipe.description"
        :image="recipe.image"
        :ingredients="recipe.ingredients"
        @delete-recipe="deleteRecipe"
        @edit-recipe="editRecipe"
      />
    </div>
  </div>
</template>
<script lang="ts">
import SingleRecipe from "~/components/singleRecipe.vue";
import base64 from "~/components/base64.vue";
import axios from "axios";

interface Ingredients {
  name: String;
  number: Number;
  unit: String;
}
[];
export default {
  components: {
    SingleRecipe,
  },
  setup() {
    const route = useRoute();

    const addRecipeDialog = ref(false);
    const id = ref(null);
    const title = ref("");
    const description = ref("");
    const image = ref("");
    const ingredients = ref([
      {
        name: "",
        number: 0,
        unit: "",
      },
    ]);

    const recipes = ref([
      {
        id: 1,
        title: "Recept 1",
        description: "Opis recepta 1",
        image:
          "https://www.foodandwine.com/thmb/fjNakOY7IcuvZac1hR3JcSo7vzI=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/FAW-recipes-pasta-sausage-basil-and-mustard-hero-06-cfd1c0a2989e474ea7e574a38182bbee.jpg",
        ingredients: [
          {
            name: "Brašno",
            number: 100,
            unit: "g",
          },
          {
            name: "Ulje",
            number: 20,
            unit: "ml",
          },
          {
            name: "Jaja",
            number: 1,
            unit: "kom",
          },
        ],
      },
    ]);

    onMounted(() => {
      getAllRecipes();
    });

    function getAllRecipes() {
      let linkstr = "http://localhost:8080/api/recipes/getAllRecipes";

      axios
        .get(linkstr)
        .then((response: any) => {
          console.log("response");
          console.log(response.data);
          recipes.value = response.data.map((e: any) => {
            return {
              id: e.id,
              title: e.title,
              description: e.description,
              image: e.image,
              ingredients: JSON.parse(e.ingredients),
            };
          });
          console.log("response");
        })
        .catch((e: any) => {
          console.error("GRESKA U POZIVU");
          console.error(e);
        });
    }

    function addRecipe() {
      let linkstr = "http://localhost:8080/api/recipes/addRecipe";

      let data = {
        title: title.value,
        description: description.value,
        image: image.value,
        ingredients: JSON.stringify(
          ingredients.value.filter(
            (e) =>
              e.name &&
              e.name != "" &&
              e.number &&
              e.number != 0 &&
              e.unit &&
              e.unit != ""
          )
        ),
      };

      let data1 = JSON.stringify(data);
      axios
        .post(linkstr, data1, {
          headers: {
            "Content-Type": "application/json",
          },
        })
        .then((response: any) => {
          console.log("response 1");
          console.log(response.data);

          console.log("response 1");
          closeRecipeDialog();
          getAllRecipes();
        })
        .catch((e: any) => {
          console.error("GRESKA U POZIVU");
          console.error(e);
        });
    }
    function deleteRecipe(id: number) {
      let linkstr = "http://localhost:8080/api/recipes/deleteRecipe/" + id;

      axios
        .delete(linkstr)
        .then((response: any) => {
          console.log("response");
          console.log(response.data);
          getAllRecipes();
          console.log("response");
        })
        .catch((e: any) => {
          console.error("GRESKA U POZIVU");
          console.error(e);
        });
    }
    function editRecipe(recipeData: any) {
      id.value = recipeData.id;
      title.value = recipeData.title;
      description.value = recipeData.description;
      image.value = recipeData.image;
      ingredients.value = recipeData.ingredients;

      addRecipeDialog.value = true;
    }
    function editRecipePut() {
      let linkstr = "http://localhost:8080/api/recipes/editrecipe/" + id.value;

      let data = {
        title: title.value,
        description: description.value,
        image: image.value,
        ingredients: JSON.stringify(
          ingredients.value.filter(
            (e) =>
              e.name &&
              e.name != "" &&
              e.number &&
              e.number != 0 &&
              e.unit &&
              e.unit != ""
          )
        ),
      };

      let data1 = JSON.stringify(data);
      axios
        .put(linkstr, data1, {
          headers: {
            "Content-Type": "application/json",
          },
        })
        .then((response: any) => {
          console.log("response 1");
          console.log(response.data);

          console.log("response 1");
          closeRecipeDialog();
          getAllRecipes();
        })
        .catch((e: any) => {
          console.error("GRESKA U POZIVU");
          console.error(e);
        });
    }

    function addIngredient() {
      ingredients.value.push({
        name: "",
        number: 0,
        unit: "",
      });
    }

    function closeRecipeDialog() {
      id.value = null;
      title.value = "";
      description.value = "";
      image.value = "";
      ingredients.value = [
        {
          name: "",
          number: 0,
          unit: "",
        },
      ];

      addRecipeDialog.value = false;
    }

    function getImage(files: any) {
      // Dobijem sliku koju je okacio i zovem ovu funkciju ispod sa slikom i zeljenom sirinom
      downscaleImage(files.base64, 630, "image/jpeg", 0.7);
    }

    function downscaleImage(
      dataUrl: string,
      newWidth: number,
      imageType: string,
      imageArguments: number
    ) {
      "use strict";
      var image1: HTMLImageElement,
        oldWidth: number,
        oldHeight: number,
        newHeight: number,
        canvas: HTMLCanvasElement,
        ctx: CanvasRenderingContext2D | null,
        newDataUrl: string;
      debugger;
      // Provide default values
      imageType = imageType || "image/jpeg";
      imageArguments = imageArguments || 0.7;

      // Create a temporary image so that we can compute the height of the downscaled image.
      image1 = new Image();
      image1.src = dataUrl;
      // Sacekamo da se slika ucita inace nece raditi
      image1.onload = function () {
        // izracunamo visinu na osnovu zeljene sirine
        oldWidth = image1.width;
        oldHeight = image1.height;
        newHeight = Math.floor((oldHeight / oldWidth) * newWidth);

        // Create a temporary canvas to draw the downscaled image on.
        canvas = document.createElement("canvas");
        canvas.width = newWidth;
        canvas.height = newHeight;

        // Draw the downscaled image on the canvas and return the new data URL.
        ctx = canvas.getContext("2d");
        ctx?.drawImage(image1, 0, 0, newWidth, newHeight);
        newDataUrl = canvas.toDataURL(imageType, imageArguments);

        image.value = newDataUrl;
        // i sada samo sacuvamo to tamo gde nam treba
        return newDataUrl;
      };
    }
    return {
      route,
      recipes,
      addRecipeDialog,
      id,
      title,
      description,
      image,
      ingredients,
      addIngredient,
      addRecipe,
      closeRecipeDialog,
      deleteRecipe,
      editRecipe,
      editRecipePut,
      getImage,
    };
  },
};
</script>
