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
            @click="addRecipe"
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
      />
    </div>
  </div>
</template>
<script lang="ts">
import SingleRecipe from "~/components/singleRecipe.vue";
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
      {
        title: "Recept 2",
        description: "Opis recepta 2",
        image:
          "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTROsR7YTv67gUzkQ3W1S6Y6N0REbyC7I8rIQ&s",
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
        image:
          "https://www.foodiesfeed.com/wp-content/uploads/2023/06/burger-with-melted-cheese.jpg",
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
    function addIngredient() {
      ingredients.value.push({
        name: "",
        number: 0,
        unit: "",
      });
    }

    function closeRecipeDialog() {
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
    return {
      route,
      recipes,
      addRecipeDialog,
      title,
      description,
      image,
      ingredients,
      addIngredient,
      addRecipe,
      closeRecipeDialog,
      deleteRecipe,
    };
  },
};
</script>
