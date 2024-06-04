<script setup lang="ts">
import { buttonVariants } from "@/components/ui/button"
import RemovePinDialog from '@/components/navigation/RemovePinDialog.vue'

defineProps({
    to: { type: String, required: true },
    name: { type: String, required: true }
})

</script>

<template>
    <div class="group px-2 border-none hover:bg-accent/50"
                    :class="buttonVariants({ variant: 'outline', alignment: 'between', size: 'xs' })">
                    <RouterLink class="hover:underline" :to="`/${to}`">{{ name }}</RouterLink>
                    <RemovePinDialog :id="to" />
                </div>
</template>