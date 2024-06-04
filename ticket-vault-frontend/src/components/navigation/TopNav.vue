<script setup lang="ts">
import { buttonVariants } from "@/components/ui/button"
import TicketSearch from "@/components/navigation/TicketSearch.vue";
import { Vault } from 'lucide-vue-next'

import ModeToggle from "@/components/ui/ModeToggle.vue";
import MobileDrawer from "@/components/navigation/MobileDrawer.vue";

import { useRoute } from 'vue-router';

// Access the current route
const route = useRoute();

// Dynamically set the variant based on the current route
function active(routeName: String) {
  const isActive = route.name === routeName;
  const variant = isActive ? 'linkOutlineActive' : 'linkOutline';
  return variant;
}
</script>

<template>
    <nav class="border-b border-primary/15 px-5 md:px-10 py-3">
        <div class="flex items-center">
            <RouterLink :to="{ name: 'tickets' }"><Vault class="w-8 h-8 me-4 lg:me-2" /></RouterLink>
            
            <h2 class="text-xl font-bold hidden lg:block">Ticket&nbsp;Vault</h2>
        </div>
        <div class="flex flex-1 items-center sm:justify-between">
            <TicketSearch />
            <div class="space-x-2 sm:space-x-4 items-center self-end flex">
                <div class="hidden md:flex space-x-4">

                    <RouterLink :to="{ name: 'tickets' }"
                        :class="buttonVariants({ variant: active('tickets'), size: 'xs' })">
                        Overview</RouterLink>

                        <RouterLink :to="{ name: 'import' }" :class="buttonVariants({ variant: active('import'), size: 'xs' })">
                        Import</RouterLink>

                    <RouterLink :to="{ name: 'tickets' }" 
                    :class="buttonVariants({ variant: active('export'), size: 'xs' })">
                        Export</RouterLink>

                </div> 
                <MobileDrawer />
                <div class="w-[1px] h-[18px] bg-foreground/30 ">&nbsp;</div>
                <ModeToggle />
            </div>
        </div>
    </nav>
</template>