<script setup lang="ts">
import PinnedTicket from '@/components/navigation/PinnedTicket.vue'
import SavedFilter from "@/components/navigation/SavedFilter.vue"


const props = defineProps({
  pins: {type: Array}
})

</script>
<template>
    <nav class="border-r border-primary/15 px-5 py-5 ">
        <h6 class="text-md font-bold" >Pinned Tickets</h6>
        <div class="flex flex-col pt-2 pb-5" v-if="pins?.length > 0">
            <PinnedTicket v-for="n in pins" v-bind:to="n.issueKey" v-bind:name="`Ticket ${n.issueKey}`" />
        </div>
        <p class="text-sm " v-else>No tickets pinned</p>
        <!-- <h6 class="text-md font-bold">Saved Filters</h6>
        <div class="flex flex-col pt-2 pb-5">
            <SavedFilter v-for="n in 4" v-bind:to="`tickets`" v-bind:name="`Filter ${n}`" />
        </div> -->
    </nav>
</template>