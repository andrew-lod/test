<script setup lang="ts">
import { buttonVariants } from "@/components/ui/button"
import RemoveFilterDialog from '@/components/navigation/RemoveFilterDialog.vue'

defineProps({
    to: { type: String, required: true },
    name: { type: String, required: true }
})

</script>

<template>
    <div class="group px-2 border-none hover:bg-accent/50"
                    :class="buttonVariants({ variant: 'outline', alignment: 'between', size: 'xs' })">
                    <RouterLink class="hover:underline" :to="{ name: 'tickets' }">{{ name }}</RouterLink>
                    <RemoveFilterDialog />
                </div>
</template>