<script setup lang="ts">
import { type HTMLAttributes, computed, type VNodeRef, ref } from 'vue'
import {
  ScrollAreaCorner,
  ScrollAreaRoot,
  type ScrollAreaRootProps,
  ScrollAreaViewport,
} from 'radix-vue'
import ScrollBar from './ScrollBar.vue'
import { cn } from '@/lib/utils'

const props = defineProps<ScrollAreaRootProps & { class?: HTMLAttributes['class'], onScroll?: Function, hideScrollbar?: Boolean}>()

const viewportRef = ref<HTMLElement | null>(null);

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props

  return delegated
})

const expose = (expose: (arg0: { viewportRef: typeof viewportRef }) => void) => {
  expose({ viewportRef });
};
</script>

<template>
  <ScrollAreaRoot v-bind="delegatedProps" :class="cn('relative overflow-hidden', props.class)">
    <ScrollAreaViewport ref="viewportRef" class="h-full w-full rounded-[inherit]" @scroll="$props.onScroll" >
      <slot />
    </ScrollAreaViewport>
    <ScrollBar v-show="!props.hideScrollbar"/>
    <ScrollAreaCorner />
  </ScrollAreaRoot>
</template>
