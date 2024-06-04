<script setup lang="ts">
import { Badge } from '@/components/ui/badge'

import type { Comment } from '@/types/Ticket'

defineProps<{
    comment: Comment,
}>()

function formatDate(date: Date) {
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    date = new Date(date);
    return date.toLocaleDateString(undefined, options);
}
</script>

<template>
    <div class="space-x-2 border bg-muted/20 rounded-md p-1">
        <Badge variant="outline" class="ml-1">{{ comment.author }}</Badge>
        <Badge variant="default" class="ml-1">{{ formatDate(comment.createdAt) }}</Badge>
        <p class="text-sm mt-1">{{ comment.content }}</p>
    </div>
</template>