<script setup lang="ts">
import { RouterView, RouterLink } from 'vue-router'
import { buttonVariants } from "@/components/ui/button"
import CollapsibleFilters from '@/components/CollapsibleFilters.vue'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Badge } from '@/components/ui/badge'

import { LoaderCircle  } from 'lucide-vue-next'

import {
    ResizableHandle,
    ResizablePanel,
    ResizablePanelGroup,
} from '@/components/ui/resizable'

import { onMounted, ref, type Ref, type VNodeRef, watch, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'


import type { PagedTickets, TicketOverview, SelectedFields } from '@/types/PagedTickets'

const hideScrollbar = ref<Boolean>(false);
const route = useRoute()
const router = useRouter()

const loading = ref(false)
const error = ref<string | null>(null)

const tickets: Ref<TicketOverview[]> = ref([])
const nextPage: Ref<number> = ref(0)
const totalPages: Ref<number | null> = ref(null)

let lastKeywords: string[] | null = null;

const scrollPosition = ref(0);
const scrollAreaRef = ref<{ viewportRef: HTMLElement | null }>({ viewportRef: null });


// watch the params of the route to fetch the data again
// watch(() => route.query.keywords, fetchData, { immediate: true })

function arraysEqual(a: string[], b: string[]): boolean {
  if (a.length !== b.length) return false;
  for (let i = 0; i < a.length; i++) {
    if (a[i] !== b[i]) return false;
  }
  return true;
}

function reset(keywords: string[] = []) {
        lastKeywords = keywords
        tickets.value = []
        nextPage.value = 0
        totalPages.value = null
        selectedFilters = {}
}

let selectedFilters : SelectedFields = {}

const fetchData = async () => {
    if(loading.value) return
    if (scrollAreaRef.value.viewportRef) {
    scrollPosition.value = scrollAreaRef.value.viewportRef.scrollTop;
    }

    const keywords = route.query.keywords?.toString().split(',') ?? []
    const { project, issueType, resolution, status, labels } = route.query as SelectedFields
    if(selectedFilters.project != project)
        reset(keywords)
    
    if(selectedFilters.issueType != issueType)
        reset(keywords)

    if(selectedFilters.resolution != resolution)
        reset(keywords)

    if(selectedFilters.status != status)
        reset(keywords)

    if(selectedFilters.labels != labels)
        reset(keywords)

    if(lastKeywords != null && !arraysEqual(keywords, lastKeywords))
        reset(keywords)

    if(totalPages.value != null && nextPage.value > totalPages.value - 1) {
        return
    }
    
    try {
        loading.value = true;
        const params = new URLSearchParams();
        
        if (keywords.length > 0) 
            params.set('keywords', keywords.toString());
        
        // if (project)
        //     params.set('project', project.toString());

        if (issueType)
            params.set('issueType', issueType.toString());

        if (resolution)
            params.set('resolution', resolution.toString());

        if (status)
            params.set('status', status.toString());

        if (labels)
            params.set('labels', labels.toString());

        params.set('page', nextPage.value.toString());

        const res = await fetch('http://localhost:8080/tickets?' + params, {
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        })

        if (!res.ok) {
            const errorMsg = await res.text()
            throw new Error(errorMsg)
        }

        const { content, totalPages: total } = await res.json() as PagedTickets
        nextPage.value = nextPage.value + 1
        totalPages.value = total
        tickets.value = [...tickets.value, ...content]

        lastKeywords = keywords
        selectedFilters.project = project
        selectedFilters.issueType = issueType
        selectedFilters.resolution = resolution
        selectedFilters.status = status
        selectedFilters.labels = labels


        await nextTick();
        if (scrollAreaRef.value.viewportRef) {
        scrollAreaRef.value.viewportRef.scrollTop = scrollPosition.value;
        }

    } catch (err: any) {
        if (err instanceof Error) {
            error.value = err.message
        }
    }
    loading.value = false
}

watch(
  () => route.query.keywords, 
  fetchData,
  { immediate: true }
)

watch(() => route.query.issueType, fetchData, { deep: true })
watch(() => route.query.resolution, fetchData, { deep: true })
watch(() => route.query.status, fetchData, { deep: true })
watch(() => route.query.labels, fetchData, { deep: true })

const onScroll = (event: Event) => {
  const target = event.target as HTMLElement;
  if (target.scrollTop >= target.scrollHeight - target.clientHeight - 50 && !loading.value) {
    fetchData();
  }
};

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
        //Uncaught (in promise) SyntaxError: \ at end of pattern
        if(word.length == 0)
            continue

        while(word.endsWith('\\')) {
            word = word.slice(0, -1)
        }            
        content = content.replace(new RegExp(word, 'gi'), (match) => `<span class="bg-yellow-300/25">${match}</span>`)
    }
    return content;
}



// watch(() => selectedFilters.value, () => {
//     const keywords = route.query.keywords?.toString().split(',') ?? []
//     reset(keywords)
//     fetchData()
// }, { deep: true })


</script>

<template>
    <main class="flex flex-col min-h-0 col-span-2 lg:col-span-1 overflow-hidden relative">
        <CollapsibleFilters class="border-b w-full bg-muted/50" />

        <ResizablePanelGroup id="handle-demo-group-1" direction="horizontal">
            <ResizablePanel id="handle-demo-panel-1" :min-size="20" :max-size="60">
                <div class="h-full grid w-screen md:w-full absolute md:static">
                    <ScrollArea ref="scrollAreaRef" class="bg-background flex flex-col custom-width" @scroll="onScroll">
                        <div class="flex flex-col  w-full bg-muted dark:bg-black/50">
                            <div class="border-b px-3 py-2 hover:bg-secondary/30 bg-background" v-for="ticket in tickets">
                                <RouterLink class="2" :to="{ path: ticket.issueKey.toString(), query: $route.query }">
                                    <Badge variant="secondary" v-html="highlight(ticket.issueKey.toString())"></Badge>
                                    <p class="ps-1 py-1 text-sm" v-html="highlight(ticket.summary)"></p>
                                </RouterLink>
                            </div>
                            <div v-if="loading" class="flex flex-1 items-center justify-center py-5">
                                <LoaderCircle class="text-primary/50 animate-spin w-10 h-10" />
                            </div>

                        </div>
                    </ScrollArea>
                </div>

            </ResizablePanel>
            <ResizableHandle id="handle-demo-handle-1" class="hidden md:flex" with-handle />
            <ResizablePanel id="handle-demo-panel-2" class="" :default-size="90">
                <div class="flex h-full bg-muted/30">
                    <RouterView />
                </div>
            </ResizablePanel>
        </ResizablePanelGroup>

    </main>
</template>

<style>
/* @media (max-width: 768px) {
    .custom {
        flex: unset!important;
        width: 100%;
    }
} */

.custom-width>div {
    display: flex;
}

.custom-width>div>div {
    display: flex !important;
}
</style>