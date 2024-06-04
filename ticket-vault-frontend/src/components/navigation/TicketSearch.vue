<script setup lang="ts">
import { TagsInput, TagsInputInput, TagsInputItem, TagsInputItemDelete, TagsInputItemText } from '@/components/ui/tags-input'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Search } from 'lucide-vue-next'
import { ref, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ScrollArea, ScrollBar } from '@/components/ui/scroll-area'

const router = useRouter()
const route = useRoute()

const keywords = ref<string[]>([])

watch(
  () => route.query.keywords, 
  () => keywords.value = route.query.keywords?.toString().split(',') ?? [], 
  { immediate: true }
)

// onMounted(() => {
//   keywords.value = route.query.keywords?.toString().split(',') ?? []
// })

function search() {
  const currentRouteName = route.name;
  if (currentRouteName === 'tickets' || currentRouteName === 'ticket') {
    const path = route.params.id ? '/' + route.params.id : '/';

    // Create a new query object by spreading the existing query parameters.
    const existingQuery = { ...route.query };

    // Filter out empty keywords and join them with commas if they exist.
    const keywordString = keywords.value.filter(keyword => keyword.trim()).join(',');
    if (keywordString) {
      existingQuery.keywords = keywordString;
    } else {
      // If there are no keywords, ensure the 'keywords' property is removed.
      delete existingQuery.keywords;
    }

    // Update the route with the new or updated query parameters.
    router.push({ path, query: existingQuery });
  }
}

//  do something when keywords ref change
watch(keywords, () => {
  
  const filtered = keywords.value.filter(keyword => keyword.trim() !== '')
  if(filtered.length !== keywords.value.length) {
    keywords.value = filtered.map(keyword => keyword.trim())
  }
  search()
}, { deep: true })


</script>

<template>
  <form @submit.prevent="search()" class="sm:me-5 lg:ps-8 grid grid-cols-[auto_minmax(auto,_1fr)] grid-flow-col flex-1 space-x-3">
    <ScrollArea>
      <TagsInput v-model="keywords" class="flex-nowrap items-center mx-0 px-2 gap-2 py-1">
      <TagsInputItem v-for="item in keywords" :key="item" :value="item">
        <TagsInputItemText />
        <TagsInputItemDelete />
      </TagsInputItem>

      <TagsInputInput placeholder="Keywords..." class="" />
    </TagsInput>
      <ScrollBar orientation="horizontal" />
    </ScrollArea>

    <Button type="submit" size="icon" class="w-9 h-8 bg-foreground">
      <Search class="w-5 h-5" />
    </Button>
  </form>
</template>