<script setup lang="ts">
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'

import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger,
} from '@/components/ui/tooltip'
import { Pin, FileUp } from 'lucide-vue-next'
import { ScrollArea } from '@/components/ui/scroll-area'
import { watch, ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import CommentsCollapsible from '@/components/CommentsCollapsible.vue'

import CustomValueDialog from '@/components/CustomValueDialog.vue'

import type { TicketComplete } from '@/types/Ticket'
import Comment from '@/components/Comment.vue'
import AttachmentBadge from '@/components/AttachmentBadge.vue'

const route = useRoute()

const ticket = ref<TicketComplete | null>(null)
const error = ref<String | null>(null)

const props = defineProps<{
    id: String
}>()

watch(() => route.params.id, fetchData, { immediate: true });

async function fetchData() {
    error.value = ticket.value = null
    if (!props.id) {
        return
    }
    
    try {
        const res = await fetch(`http://localhost:8080/tickets/${props.id}`)
        if(!res.ok) {
            const errorMsg = await res.text()
            throw new Error(errorMsg)
        }
        const data: TicketComplete = await res.json()
        ticket.value = data
    } catch (err: any) {
        if(err instanceof Error) {
            error.value = err.message
        }
    } 
}

function escapeHtml(content : String) {
    return content.replace(/&/g, '&amp;')
                  .replace(/</g, '&lt;')
                  .replace(/>/g, '&gt;')
                  .replace(/"/g, '&quot;')
                  .replace(/'/g, '&#039;');
}

function highlight(content : String) {
    content = escapeHtml(content);
    const keywords = route.query.keywords?.toString().split(',') ?? []
    if(!keywords)
        return content;
    for(let word of keywords) {
        if(word.length == 0)
            continue

        while(word.endsWith('\\')) {
            word = word.slice(0, -1)
        }            
        content = content.replace(new RegExp(word, 'gi'), (match) => `<span class="bg-yellow-300/25">${match}</span>`)
    }
    return content;
}

import { useToast } from '@/components/ui/toast/use-toast'

const { toast } = useToast()

const addingPin = ref(false)

const addPin = async () => {
    if(addingPin.value)
        return
    addingPin.value = true


    const formData = new FormData()
    formData.append('issueKey', route.params.id.toString())

    try{
        console.log('Pin ticket')
        const res = await fetch(`http://localhost:8080/pins`, {
            method: 'POST',
            body: formData
        })

        if(!res.ok) {
            const errorMsg = await res.text()
            throw new Error(errorMsg)
        } 
        location.reload()
    } catch (err: any) {
        toast({
            title: 'Pin ticket error',
            description: err.message,
            variant: 'destructive'
        })
    }
    addingPin.value = false
}

function refresh() {
    // console.log('refresh')
    fetchData()
    // fetchData()
}

</script>

<template>
    <div v-if="!id" class="flex-1 custom-hide hidden md:flex items-center justify-center bg-green">
        <p class="text-foreground/50 text-center">Selecteer een ticket uit de lijst</p>
    </div>

    <div v-if="id" class="visible custom bg-background w-full h-full min-h-0 grid grid-cols-1 grid-rows-[auto_1fr] ">
        <div v-if="error" class="bg-red-100 text-red-800 p-4">
            <p>{{ error }}</p>
        </div>  

        <div v-else class="flex items-center px-4 py-2 justify-end w-full bg-muted/20 border-b">
            <div class="flex items-center space-x-4">
                <TooltipProvider>
                    <Tooltip>
                        <TooltipTrigger as-child>
                            <Button variant="ghost" size="icon" class="h-6 w-6" @click="addPin">
                                <Pin class="h-5 w-5" />
                            </Button>
                        </TooltipTrigger>
                        <TooltipContent>
                            <p>Pin ticket</p>
                        </TooltipContent>
                    </Tooltip>
                </TooltipProvider>
                <!-- <TooltipProvider>
                    <Tooltip>
                        <TooltipTrigger as-child>
                            <Button variant="ghost" size="icon" class="h-6 w-6">
                                <FileUp class="h-5 w-5" />
                            </Button>
                        </TooltipTrigger>
                        <TooltipContent>
                            <p>Export ticket</p>
                        </TooltipContent>
                    </Tooltip>
                </TooltipProvider> -->

            </div>
        </div>

        <ScrollArea class="bg-background custom-width" >
            <div class="pb-[65px] lg:pb-0 bg-muted dark:bg-black/50 flex flex-col w-full">

                

                <div class="p-6 bg-background border-b" v-if="ticket">

                

                    <Badge variant="secondary" v-html="highlight(ticket.issueKey.toString())"></Badge>

                    <CustomValueDialog :objectId="ticket.archivedTicketId" @refresh="refresh"/>
                    
                    <p class="ps-1 py-3 text-xl font-semibold" v-html="highlight(ticket.summary)"></p>
                    <div class="flex space-x-2 py-3 border-b">
                        <Badge variant="secondary">{{ ticket.issueType }}</Badge>
                        <Badge variant="secondary">{{ ticket.resolution }}</Badge>
                        <Badge variant="secondary">{{ ticket.status }}</Badge>
                    </div>
                    <div class="py-2 flex flex-wrap justify-center">
                        <AttachmentBadge v-for="attachment in ticket.attachments" :attachment="attachment" />
                    </div>
                    <h2 class="font-semibold my-2 ps-1">Description</h2>
                    <p class="ps-1 mb-5" v-html="highlight(ticket.description)"></p>

                    <h2 class="font-semibold" v-if="ticket.customValues && Object.keys(ticket.customValues).length > 0">Custom values</h2>
                    <div v-if="ticket.customValues && Object.keys(ticket.customValues).length > 0" class="grid grid-cols-2 gap-2 border rounded-md py-2 px-3 bg-primary/5 mb-5">
                        <div v-for="(value, key) in ticket.customValues" :key="key">
                            <p class="font-semibold text-sm underline">{{ key }}</p>
                            <p>{{ value }}</p>
                        </div>
                    </div>


                    <CommentsCollapsible v-if="ticket.comments.length > 5" :comments="ticket.comments" />

                    <div class="flex flex-col space-y-2" v-else>
                        <h4 class="text-md font-semibold">
                            Comments <span class="text-xs">({{ ticket.comments.length }} total)</span>
                        </h4>
                        <Comment v-for="comment in ticket.comments" :comment="comment" />
                    </div>

                </div>
            </div>

        </ScrollArea>


    </div>
</template>

<style>
@media (max-width: 768px) {
    .custom {
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        z-index: 9;
        width: 100%;
    }

    .custom-hide {
        display: none;
    }
}
</style>