<script setup lang="ts">
import { Badge } from '@/components/ui/badge'
import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger
} from '@/components/ui/tooltip'
import { FileImage, FileText, FileSpreadsheet, ScrollText, Database, Paperclip } from 'lucide-vue-next'
import type { Attachment } from '@/types/Ticket';

const props = defineProps<{
    attachment: Attachment
}>()
// attachment.name = 55642_onterecht_voorlopig_afgewezen_obv_extraVak.docx
// get filename extension by splitting the string at the last dot
const extension = props.attachment.name.split('.').pop()

// if name is longer than 15 characters, shorten it
const splitName = props.attachment.name.length > 16 ? props.attachment.name.slice(0, 15) + '...' : props.attachment.name

</script>

<template>
    <TooltipProvider>
        <Tooltip>
            <TooltipTrigger><a :href="attachment.url" class="me-2 mt-1" target="_blank" alt="hoi">
                    <Badge variant="outline" class="text-center items-center">
                        <FileImage class="h-4 w-4" v-if="extension?.match(`png|jpg|jpeg|bmp`)" />
                        <FileText class="h-4 w-4" v-else-if="extension?.match(`docx|txt`)" />
                        <FileSpreadsheet class="h-4 w-4" v-else-if="extension?.match(`xlsx`)" />
                        <ScrollText class="h-4 w-4" v-else-if="extension?.match(`log`)" />
                        <Database class="h-4 w-4" v-else-if="extension?.match(`sql`)" />
                        <Paperclip class="h-4 w-4" v-else />
                        <p>{{ splitName }}</p>
                    </Badge>
                </a></TooltipTrigger>
            <TooltipContent>
                <p>{{ attachment.name }}</p>
            </TooltipContent>
        </Tooltip>
    </TooltipProvider>

</template>