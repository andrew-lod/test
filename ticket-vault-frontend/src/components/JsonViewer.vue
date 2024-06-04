<script setup lang="ts">
import { defineProps, onMounted, ref, watch } from 'vue';
import JsonViewer from '@/components/JsonViewer.vue'; // Import itself for recursion
import { Button } from '@/components/ui/button'


const props = defineProps({
  jsonData: { type: [Object, String], required: true },
  path: { type: Array, default: () => [] }
});

const emit = defineEmits(['keyClicked', 'customValueAdded']);


function isObject(value: any) {
  return value !== null && typeof value === 'object';
}

function emitKeyClicked(key: any, value: any) {
  emit('keyClicked', [...props.path, key], value);
}

function handleKeyClicked(key: any, value: any) {
  emit('keyClicked', key, value);
}


function stringifyValue(value : any) {
  if (typeof value === 'string') return `"${value}"`;
  return JSON.stringify(value);
}



</script>

<template>
        <div class="json-tree ml-2 text-xs whitespace-pre font-mono">
            <span v-if="isObject(jsonData)"></span>
            <ul v-if="isObject(jsonData)">
                <li v-for="(value, key) in jsonData" :key="key">
                <Button @click="emitKeyClicked(key, value)" class="text-yellow-600 border cursor-pointer font-mono text-xs h-5 p-0 my-[1px]" variant="outline">{{ key }}</Button><span>:</span>
                <span v-if="isObject(value)" class="json-object">
                    { <JsonViewer :json-data="value" :path="[...path, key]" @keyClicked="handleKeyClicked" /> },
                </span>
                <span v-else class="text-green-600">&nbsp;{{ stringifyValue(value) }}<span class="text-black">,</span></span>
                </li>
            </ul>
            <span v-if="isObject(jsonData)"></span>
        </div>
</template>

