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
import { Trash2 } from 'lucide-vue-next'
</script>

<template>
    <Dialog>
        <DialogTrigger as-child>
            <Button variant="link" size="none" class="hidden group-hover:block w-4 h-4">
                <Trash2 class="hover:text-destructive w-4 h-4" />
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

                <Button type="submit" variant="destructive" class="mb-2">
                    Remove
                </Button>
            </DialogFooter>
        </DialogContent>
    </Dialog>
</template>