<script setup lang="ts">
import { ref, watch, defineProps } from 'vue'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogClose,
  DialogScrollContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

import { LoaderCircle } from 'lucide-vue-next'

import JsonViewer from '@/components/JsonViewer.vue'
import { sys } from 'typescript'

import type { CustomValueMapping, Route } from '@/types/Mapping'

const props = defineProps({
  objectId: String
})

const error = ref<string | null>(null)
const loading = ref(false)
const open = ref(false)
const saving = ref(false)

const saveError = ref<string>()

const valueName = ref<string>()

const jsonData = ref<string>()

const selectedRoute = ref<Array<String>>([]);

const onKeyClicked = (path: any, value: any) => {
  // Get the keys from the path
  const keys = path.map((key: any) => key.toString());
  // Show a form or modal to capture route and processing options
  console.log(`Key Path: ${path.join(' -> ')}`, value);
  selectedRoute.value = keys;
};


const saveCustomValue = async() => {
  saveError.value = ''
  if(!valueName.value) 
    return saveError.value = 'Please enter a value name'

  if(selectedRoute.value.length === 0)
    return saveError.value = 'Please select a route to save the value'

  saving.value = true
  console.log('Saving custom value...' + valueName.value)

  const createRouteLinkedList = (index: number): Route | null => {
    if (index >= selectedRoute.value.length) return null;

    return {
      path: selectedRoute.value[index].toString(),
      next: createRouteLinkedList(index + 1),
      keyProcessing: null, // Placeholder, adjust as needed
      valueProcessing: null // Placeholder, adjust as needed
    };
  };

  const customValueMapping: CustomValueMapping = {
    keyName: valueName.value,
    route: createRouteLinkedList(0) // Start the linked list creation from the first element
  };

  console.log('Custom Value:', customValueMapping);

  const customValueFormData = new FormData()
  customValueFormData.append('key', valueName.value)

  try {
    const saveAttribute = await fetch(`http://localhost:8080/tickets/attributes`, {
      method: 'POST',
      body: customValueFormData
    })

    if (!saveAttribute.ok) {
      throw new Error(await saveAttribute.text())
    }


    const saveMapping = await fetch(`http://localhost:8080/attributeMappings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(customValueMapping)
    })

    if (!saveMapping.ok) {
      throw new Error(await saveMapping.text())
    }

    const res = await fetch(`http://localhost:8080/tickets/refresh`, {
      method: 'POST'
    })

    if(!res.ok) {
      throw new Error(await res.text())
    }

    open.value = false
    // refresh page
    emit('refresh');
    // location.reload()
  } catch (err: any) {
    saveError.value = err.message
  }
  saving.value = false
}

const emit = defineEmits(['refresh']);


// const objectId = ref(props.objectId)
const fetchObject = async () => {
  if (jsonData.value) return

  if (loading.value) return
  loading.value = true

  try {
    const res = await fetch(`http://localhost:8080/archived-tickets/${props.objectId}`)

    if (!res.ok) {
      throw new Error('Failed to fetch object')
    }
    jsonData.value = await res.json() as string
  } catch (err: any) {
    error.value = err.message
  }
  loading.value = false
}

watch(open, fetchObject)

function emitValueAdded() {
  emit('refresh');
}

</script>

<template>
  <Dialog v-model:open="open">
    <DialogTrigger as-child>
      <Button variant="default" class="h-5">
        JSON-Object
      </Button>
    </DialogTrigger>
    <DialogScrollContent class="flex flex-col w-auto md:w-auto max-w-[90vw] max-h-[90vh]">
      <DialogHeader>
        <DialogTitle>JSON-viewer</DialogTitle>
        <DialogDescription>
          See the old archived JSON object here and add a custom value by selecting a key.
        </DialogDescription>
      </DialogHeader>
      <div v-if="jsonData" class="border py-2 overflow-y-auto">
        <JsonViewer :jsonData="jsonData" @keyClicked="onKeyClicked" @customValueAdded="emitValueAdded"/>
        <!-- {{ jsonData }} -->
        <!-- <pre class=" whitespace-pre-wrap p-5">{{ jsonData }}</pre> -->
      </div>
      <div v-else>
        <p v-if="error" class="text-destructive">{{ error }}</p>
        <LoaderCircle class="animate-spin text-primary w-10 h-10" v-if="loading" />
      </div>
      <DialogFooter>
        
        <div class="flex flex-col justify-between w-full items-start">

          <div class="flex w-full">
            <div class="grid w-full max-w-sm items-center gap-1.5 mb-3">
              <Label for="vname">Custom value name</Label>
              <Input id="vname" type="text" v-model="valueName" placeholder="Custom value name" :disabled="saving" />
            </div>
            <div class="mb-3 ms-5">
              <h5>
                Selected Route
              </h5>
              <span v-for="(key, index) in selectedRoute" :key="index">
                <span class=" text-yellow-600">"{{ key }}"</span>
                <span v-if="index < selectedRoute.length - 1" class=" text-green-600"> -> </span>
              </span>
              <span v-if="selectedRoute.length === 0" class=" text-red-600">No route selected</span>
            </div>
          </div>

          <div class="flex items-center">
            <DialogClose as-child>
              <Button type="button" variant="secondary" :disabled="saving">
                Close without saving
              </Button>
            </DialogClose>

            <Button type="submit" @click="saveCustomValue" :disabled="saving" class="ms-5">
              Save changes
            </Button>
            <LoaderCircle class="animate-spin text-primary ms-5" v-if="saving" />
            <p v-if="saveError" class="text-destructive ms-5">{{ saveError }}</p>
          </div>

        </div>

      </DialogFooter>
    </DialogScrollContent>
  </Dialog>
</template>