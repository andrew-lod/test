<script setup lang="ts">
import { Button } from '@/components/ui/button'
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from '@/components/ui/dialog'
import { PinOff, Trash2 } from 'lucide-vue-next'

import { useToast } from '@/components/ui/toast/use-toast'

const { toast } = useToast()

const props = defineProps({
    id: { type: String, required: true },
})


const removePin = async () => {
    const formData = new FormData()
    formData.append('issueKey', props.id)
    try {
        const res = await fetch(`http://localhost:8080/pins`, {
            method: 'DELETE',
            body: formData,
        })
        if (!res.ok) {
            const errorMsg = await res.text()
            throw new Error(errorMsg)
        }
        location.reload()
    } catch (err: any) {
        if (err instanceof Error) {
            toast({
            title: 'Pin ticket error',
            description: err.message,
            variant: 'destructive'
        })
        }
    }

}

</script>

<template>
    <Dialog>
        <DialogTrigger as-child>
            <Button variant="link" size="none" class="hidden group-hover:block w-4 h-4">
                <PinOff class="hover:text-destructive w-4 h-4" />
            </Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-[425px]">
            <DialogHeader>
                <DialogTitle>Are you absolutely sure?</DialogTitle>
                <DialogDescription>
                    This action cannot be undone.
                </DialogDescription>
            </DialogHeader>
            <DialogFooter>
                <DialogClose as-child>
                    <Button>
                        Cancel
                    </Button>
                </DialogClose>

                <Button @click="removePin" type="submit" variant="destructive" class="mb-2">
                    Remove
                </Button>
            </DialogFooter>
        </DialogContent>
    </Dialog>
</template>