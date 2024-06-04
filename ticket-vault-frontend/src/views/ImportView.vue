<script setup lang="ts">
import { ref } from 'vue'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'

import { AlertCircle, LoaderCircle  } from 'lucide-vue-next'

const error = ref<string | null>(null);
const loading = ref(false);
const succes = ref<string | null>(null);
const selectedFile = ref<File | null>(null);


async function handleSubmit() {
  error.value = succes.value = null
  if (!selectedFile.value) {
    error.value = 'Please select a file to import';
    return;
  }
  loading.value = true;
  await sendImportRequest(selectedFile.value);
  loading.value = false;
}

async function sendImportRequest(file: File) {
  const form = new FormData()
  form.append('file', file)

  try {
    const res = await fetch(`http://localhost:8080/tickets`, {
      method: 'POST',
      body: form
    })
    if (!res.ok) {
      const errorMsg = await res.text()
      throw new Error(errorMsg)
    }
    succes.value = 'Tickets have been imported successfully!'
  } catch (err: any) {
    if (err instanceof Error) {
      error.value = err.message
    }
  }
}
</script>

<template>
  <main class="flex flex-col col-span-2 lg:col-span-1">
    <div class="px-8 py-10 border-b">
      <h1 class="text-2xl font-bold ">Import Tickets to System</h1>
      <p class="my-2">Select a JSON-file with ticket objects to get started</p>
      <div>
        <div v-if="error" class="border-destructive/50 border rounded-lg text-destructive 
          dark:border-destructive inline-flex space-x-4 items-center py-1 px-5 mb-2">
          <AlertCircle class="w-4 h-4" />
          <p>{{ error }}</p>
        </div>
        <div v-if="succes" class="border-primary/50 border rounded-lg text-primary 
          dark:border-primary inline-flex space-x-4 items-center py-1 px-5 mb-2">
          <AlertCircle class="w-4 h-4" />
          <p>{{ succes }}</p>
        </div>
      </div>

      <Label for="jsonImport" class="inline-block">JSON-file</Label>
      <Input id="jsonImport" class="file:text-foreground mt-2 mb-4 max-w-sm" type="file" 
             @change="selectedFile = $event.target.files[0]" />
      <div class="flex space-x-3 items-center">
      <Button type="submit" @click="handleSubmit()" :disabled="loading" size="xs" class="max-w-20">
        Submit
      </Button> 
      <LoaderCircle class="animate-spin text-primary" v-if="loading" />
    </div>
    </div>
    <div class=" flex-1 bg-muted/30 flex items-center justify-center p-10">
    </div>
  </main>
</template>
