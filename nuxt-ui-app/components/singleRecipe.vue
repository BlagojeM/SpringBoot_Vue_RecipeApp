<template>
  <div class="recipe-container flex flex-col">
    <div class="recipe-title">{{ props.title }}</div>
    <div class="recipe-description">{{ props.description }}</div>
    <img :src="props.image" class="recipe-image" />
    <div class="flex flex-col ingredient-container">
      <span
        v-for="(ingredient, index) in props.ingredients"
        :key="'ingredient - ' + index"
        class="flex justify-center"
      >
        <span class="recipe-ingredient-name">{{ ingredient.name }}:</span>
        <span class="recipe-ingredient-number">{{ ingredient.number }}</span>
        <span class="recipe-ingredient-unit">{{ ingredient.unit }}</span>
      </span>
      <UButton
        class="m-2.5"
        color="primary"
        variant="outline"
        label="Obriši"
        @click="deleteRecipe"
      />
    </div>
  </div>
</template>
<script lang="ts">
interface Ingredients {
  name: String;
  number: Number;
  unit: String;
}
[];
export default {
  props: {
    id: Number,
    title: String,
    description: String,
    image: String,
    ingredients: Object as PropType<Ingredients> | any, // TODO: Ovde ne treba da stoji any, privremeni fix da bih vise odradio
  },
  setup(props, ctx) {
    const route = useRoute();
    // setup() receives props as the first argument.

    function deleteRecipe() {
      ctx.emit("delete-recipe", props.id);
    }

    return {
      route,
      props,
      deleteRecipe,
    };
  },
};
</script>
<style scoped>
.recipe-container {
  border-radius: 15px;
  border: 2px solid #94a3b8;
  margin: 10px;
  width: min(90%, 300px);
}
.recipe-title {
  background-color: #e1e1e13e;
  font-weight: 600;
  font-size: 20pt;
  text-align: center;
  margin-bottom: 10px;
}
.recipe-description {
  font-size: 12pt;
  text-align: center;
  margin: 5px;
  font-style: italic;
  margin-bottom: 15px;
}

.recipe-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.ingredient-container {
  margin: 20px;
}
.recipe-ingredient-name {
  margin-left: 5px;
  font-style: italic;
}
.recipe-ingredient-number {
  font-weight: 600;
  margin-left: 3px;
}
.recipe-ingredient-unit {
  margin-left: 3px;
  font-weight: 600;
}
</style>
