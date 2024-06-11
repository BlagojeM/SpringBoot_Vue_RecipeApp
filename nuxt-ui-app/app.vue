<template>
  <UContainer>
    <UCard class="mt-10">
      <template #header>
        <div class="flex justify-between">
          <header>
            <nav>
              <ul class="flex row">
                <li class="mx-5"><NuxtLink to="/">Home</NuxtLink></li>
                <!-- <li class="mx-5">
                  <NuxtLink to="/singleRecipe">Single recepie</NuxtLink>
                  </li> -->
              </ul>
            </nav>
          </header>
          <h1>Welcome to the recipe site!</h1>
          <ColorScheme
            ><USelect
              v-model="$colorMode.preference"
              :options="['system', 'light', 'dark']"
          /></ColorScheme>
        </div>
      </template>
      <!-- Current Page -->
      <NuxtPage />
    </UCard>
  </UContainer>
</template>
