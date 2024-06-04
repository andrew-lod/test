<script setup lang="ts">
import { RouterView } from 'vue-router'
import TopNav from '@/components/navigation/TopNav.vue'
import Sidebar from '@/components/navigation/Sidebar.vue'
import SidebarMobile from '@/components/navigation/SidebarMobile.vue'
import { onMounted, ref } from 'vue';
import type { PinnedTicket } from '@/types/PinnedTickets'
import Toaster from '@/components/ui/toast/Toaster.vue'

const error = ref()
const pins = ref<PinnedTicket[]>()
const loading = ref(false)

async function fetchPins() {
    error.value = null
    
    try {
        const res = await fetch('http://localhost:8080/pins', {
            method: 'GET',
        })

        if (!res.ok) {
            const errorMsg = await res.text()
            throw new Error(errorMsg)
        }

        pins.value = await res.json() as PinnedTicket[]
    } catch (err: any) {
        if(err instanceof Error) {
            error.value = err.message
        }
    } 
}

onMounted(() => {
  fetchPins()
})


</script>

<template>
    <Toaster />
    <div class="h-svh grid grid-rows-[auto_1fr] grid-cols-[15rem_minmax(0,_1fr)] lg:grid-cols-[13rem_minmax(0,_1fr)]">
        
        <header class="grid grid-cols-subgrid col-span-2">
            <TopNav class="flex lg:grid col-span-2 lg:grid-cols-subgrid" />
        </header>
        <div class="grid-cols-subgrid col-span-2 grid min-h-0">
            <Sidebar class="hidden lg:block" :pins="pins" />
            <div class="flex justify-center absolute bottom-5 w-full">
              <SidebarMobile class="lg:hidden" :pins="pins"/>
            </div>
            
            <RouterView />
        </div>
    </div>
</template>