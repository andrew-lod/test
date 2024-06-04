<script setup lang="ts">
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover'

import { Button } from '@/components/ui/button'

import SavedFilter from "@/components/navigation/SavedFilter.vue"
import type { PinnedTicket } from '@/types/PinnedTickets'

import { Pin, SlidersHorizontal } from 'lucide-vue-next'

const props = defineProps({
  pins: Array
})

</script>

<template>
  <div class="border rounded-md z-10" v-if="pins">
    <Popover>
      <PopoverTrigger>
        <Button variant="outline" size="icon" class="border-none">
          <Pin class="w-5 h-5" />
        </Button>
      </PopoverTrigger>
      <PopoverContent class="lg:hidden">
        <div class="flex flex-col">
          <h3 class="text-sm font-semibold text-foreground mb-1">Pinned Tickets</h3>
          <PinnedTicket v-for="n in 4" v-bind:to="`tickets`" v-bind:name="`Ticket ${n}`" />
        </div>

      </PopoverContent>
    </Popover>
    <Popover>
      <PopoverTrigger>
        <Button variant="outline" size="icon" class="border-none">
          <SlidersHorizontal class="w-5 h-5" />
        </Button>
      </PopoverTrigger>
      <PopoverContent class="lg:hidden">
        <div class="flex flex-col">
          <h3 class="text-sm font-semibold text-foreground mb-1">Saved Filters</h3>
          <SavedFilter v-for="n in 4" v-bind:to="`tickets`" v-bind:name="`Filter ${n}`" />
        </div>
      </PopoverContent>
    </Popover>
  </div>

</template>