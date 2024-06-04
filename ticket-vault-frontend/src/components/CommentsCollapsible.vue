<script setup lang="ts">
import { ChevronsUpDown } from 'lucide-vue-next'

import { ref } from 'vue'
import { Button } from '@/components/ui/button'
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible'

import CommentComponent from '@/components/Comment.vue'

import type { Comment } from '@/types/Ticket'

const props = defineProps<{
  comments: Comment[],
}>()

// get top 5 comments
const topComments = ref<Comment[]>(props.comments.slice(0, 5))

// everything but top comments
const otherComments = ref<Comment[]>(props.comments.slice(5))
// amount of other comments left


const isOpen = ref(false)
</script>

<template>
    <Collapsible v-model:open="isOpen" class="space-y-2">
        <div class="flex items-center space-x-4 px-1">
            <h4 class="text-md font-semibold">
                Comments
                <span class="text-xs">({{ comments.length }} total)</span>
            </h4>
            <CollapsibleTrigger as-child>
                <Button variant="ghost" size="sm" class="w-9 p-0">
                    <ChevronsUpDown class="h-4 w-4" />
                    <span class="sr-only">Toggle</span>
                </Button>
            </CollapsibleTrigger>
        </div>
        <CommentComponent v-for="comment in topComments" :comment="comment" />
        <CollapsibleContent class="space-y-2">
            <CommentComponent v-for="comment in otherComments" :comment="comment" />
        </CollapsibleContent>
    </Collapsible>

</template>