<script setup lang="ts">
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from '@/components/ui/drawer'
import { Button } from '@/components/ui/button'
import { buttonVariants } from "@/components/ui/button"

import { VisuallyHidden } from 'radix-vue'

import { Menu } from 'lucide-vue-next'
</script>

<template>
  <Drawer>
    <DrawerTrigger class="md:hidden">
        <Button variant="outline" size="icon" class="border-none">
            <Menu />
        </Button>   
    </DrawerTrigger>
    <DrawerContent>
      <DrawerHeader>
        <VisuallyHidden>
          <DrawerTitle>Menu</DrawerTitle>
        <DrawerDescription>Navigation</DrawerDescription>
        </VisuallyHidden>
        
      </DrawerHeader>
      <DrawerFooter>
           
        <DrawerClose class="flex flex-col space-y-2 mb-5">
          <RouterLink class="w-full" :to="{ name: 'tickets' }"
                        :class="buttonVariants({ variant: 'default', size: 'sm' })">
                        Overview</RouterLink>
          <RouterLink class="w-full" :to="{ name: 'import' }" :class="buttonVariants({ variant: 'outline', size: 'sm' })">
                        Import</RouterLink>
          <RouterLink class="w-full" :to="{ name: 'tickets' }" :class="buttonVariants({ variant: 'outline', size: 'sm' })">
                        Export</RouterLink>
        </DrawerClose>
      </DrawerFooter>
    </DrawerContent>
  </Drawer>
</template>