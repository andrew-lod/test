<script setup lang="ts">
import { ChevronsUpDown, ListPlus, FolderUp, Delete   } from 'lucide-vue-next'

import { ref, onMounted, toRefs, watch } from 'vue'
import { Button } from '@/components/ui/button'
import {
    Collapsible,
    CollapsibleContent,
    CollapsibleTrigger,
} from '@/components/ui/collapsible'
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip'

import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectLabel,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'

import { useRouter, useRoute } from 'vue-router'



import type { SearchFields, SelectedFields } from '@/types/PagedTickets'

const isOpen = ref(false)

const route = useRoute()
const router = useRouter()
const selectedFields = ref<SelectedFields>({})

// get filters from the server
const filters = ref<SearchFields>()

const fetchFilters = async () => {
    try {
        const response = await fetch('http://localhost:8080/tickets/filters')
        
        if (!response.ok) {
            throw new Error('Failed to fetch filters')
        }

        filters.value = await response.json() as SearchFields
    } catch (error) {
        console.error(error)
    }
}

onMounted(() => {
    fetchFilters()
})

watch(
  () => route.query.project, 
  () => selectedFields.value.project = route.query.project?.toString() ?? undefined, 
  { immediate: true }
)
watch(
  () => route.query.issueType, 
  () => selectedFields.value.issueType = route.query.issueType?.toString() ?? undefined, 
  { immediate: true }
)
watch(
  () => route.query.resolution, 
  () => selectedFields.value.resolution = route.query.resolution?.toString() ?? undefined, 
  { immediate: true }
)
watch(
  () => route.query.status, 
  () => selectedFields.value.status = route.query.status?.toString() ?? undefined, 
  { immediate: true }
)
watch(
  () => route.query.labels, 
  () => selectedFields.value.labels = route.query.labels?.toString() ?? undefined, 
  { immediate: true }
)

watch(selectedFields, (newFilters) => {
  const updatedQuery = mapSelectedFiltersToQuery(newFilters);
  const cleanQuery = Object.fromEntries(Object.entries(updatedQuery).filter(([_, v]) => v != null && v !== ''));

    // add the keywords to the query
    cleanQuery.keywords = route.query.keywords?.toString() ?? '';

  // This will replace the current path and query without adding to the history stack
  router.replace({ path: route.path, query: cleanQuery });
}, { deep: true });

function mapSelectedFiltersToQuery(filters: SelectedFields): Record<string, string | string[]> {
    const query: Record<string, string | string[]> = {};
    for (const [key, value] of Object.entries(filters)) {
        if (value) {
            query[key] = Array.isArray(value) ? value.join(',') : value;
        }
    }
    return query;
}

</script>

<template>
    <Collapsible v-model:open="isOpen" class="space-y-2">
        <div class="flex items-center px-4 py-2 justify-between w-full">
            <div class="flex space-x-2 items-center">
                <CollapsibleTrigger as-child>
                    <Button variant="ghost" size="sm" class="w-4 h-4 p-0">
                        <ChevronsUpDown class="h-4 w-4" />
                        <span class="sr-only">Toggle</span>
                    </Button>
                </CollapsibleTrigger>
                <h4 class="text-sm font-semibold">
                    Filters
                </h4>
            </div>
            <div class="flex items-center space-x-4">
                <TooltipProvider>
                    <Tooltip>
                        <TooltipTrigger as-child>
                            <Button variant="ghost" size="icon" class="h-6 w-6">
                                <ListPlus class="h-5 w-5"/>
                            </Button>
                        </TooltipTrigger>
                        <TooltipContent>
                            <p>Save current filter</p>
                        </TooltipContent>
                    </Tooltip>
                </TooltipProvider>
                
            </div>

        </div>
        <CollapsibleContent class="px-4 mt-0 pb-2">
            <div v-if="filters" class="grid grid-flow-row grid-cols-1 md:grid-cols-5">
                <div class="flex flex-col me-2" v-if="filters.issueTypes.length > 0">
                    <label class="text-sm px-2">Issue&nbsp;type</label>
                    <Select v-model="selectedFields.issueType">
                        <SelectTrigger class="h-8">
                            <SelectValue placeholder="Select issue type" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectGroup>
                                <SelectLabel class="flex items-center justify-between">
                                    <h6>Issue types</h6>
                                    <Button variant="link" size="icon" v-if="selectedFields.issueType != null" class="flex h-1 items-center text-foreground" @click="selectedFields.issueType = undefined">
                                        <Delete class="w-5" />
                                    </Button>
                            </SelectLabel>
                                <SelectItem v-for="issueType in filters.issueTypes" :key="issueType" :value="issueType">
                                    {{ issueType }}
                                </SelectItem>
                                
                            </SelectGroup>
                        </SelectContent>
                    </Select>
                </div>

                <!-- Resolution Filter -->
                <div class="flex flex-col me-2" v-if="filters.resolutions.length > 0">
                    <label class="text-sm px-2">Resolution</label>
                    <Select v-model="selectedFields.resolution">
                        <SelectTrigger class="h-8">
                            <SelectValue placeholder="Select resolution" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectGroup>
                                <SelectLabel class="flex items-center justify-between">
                                    <h6>Resolutions</h6>
                                    <Button variant="link" size="icon" v-if="selectedFields.resolution != null" class="flex h-1 items-center text-foreground" @click="selectedFields.resolution = undefined">
                                        <Delete class="w-5" />
                                    </Button>
                            </SelectLabel>
                                <SelectItem v-for="resolution in filters.resolutions" :key="resolution" :value="resolution">
                                    {{ resolution }}
                                </SelectItem>
                                
                            </SelectGroup>
                        </SelectContent>
                    </Select>
                </div>

                <!-- Status Filter -->
                <div class="flex flex-col me-2" v-if="filters.statuses.length > 0">
                    <label class="text-sm px-2">Status</label>
                    <Select v-model="selectedFields.status">
                        <SelectTrigger class="h-8">
                            <SelectValue placeholder="Select status" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectGroup>
                                <SelectLabel class="flex items-center justify-between">
                                    <h6>Status</h6>
                                    <Button variant="link" size="icon" v-if="selectedFields.status != null" class="flex h-1 items-center text-foreground" @click="selectedFields.status = undefined">
                                        <Delete class="w-5" />
                                    </Button>
                            </SelectLabel>
                                <SelectItem v-for="status in filters.statuses" :key="status" :value="status">
                                    {{ status }}
                                </SelectItem>
                                
                            </SelectGroup>
                        </SelectContent>
                    </Select>
                </div>
                

                <!-- Labels Filter -->
                <div class="flex flex-col me-2" v-if="filters.labels.length > 0">
                    <label class="text-sm px-2">Labels</label>
                    <Select v-model="selectedFields.labels">
                        <SelectTrigger class="h-8">
                            <SelectValue placeholder="Select label" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectGroup>
                                <SelectLabel class="flex items-center justify-between">
                                    <h6>Labels</h6>
                                    <Button variant="link" size="icon" v-if="selectedFields.labels != null" class="flex h-1 items-center text-foreground" @click="selectedFields.labels = undefined">
                                        <Delete class="w-5" />
                                    </Button>
                            </SelectLabel>
                                <SelectItem v-for="label in filters.labels" :key="label" :value="label">
                                    {{ label }}
                                </SelectItem>
                                
                            </SelectGroup>
                        </SelectContent>
                    </Select>
                </div>

            </div>
        </CollapsibleContent>
    </Collapsible>
</template>