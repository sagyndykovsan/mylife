<template>
    <div class="flex items-center">
        <div v-if="isEditing" class="flex items-center group">
            <input type="text" class="border border-gray-400 rounded py-1 px-2 outline-indigo-400 mr-3
                disabled:border-transparent
                disabled:bg-transparent
                disabled:px-0
                disabled:w-12
                peer" :disabled="!isEditing" :value="modelValue" @input="$emit('update:modelValue', $event.target.value)">
            <IconCancel @click="disableEditing" class="hidden peer-focus:block group-hover:block cursor-pointer"/>
        </div>
        <div v-else class="flex items-center group">
            <span class="py-1 mr-3">{{ modelValue }}</span>
            <IconEdit @click="enableEditing" class="hidden group-hover:block cursor-pointer" />
        </div>
    </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import IconEdit from './icons/IconEdit.vue';
import IconCancel from './icons/IconCancel.vue';

const props = defineProps(['modelValue', 'editingCount']);
const editMode = ref(false);
const oldInputValue = props.modelValue;
const emit = defineEmits(['update:modelValue', 'increaseEditingCount', 'decreaseEditingCount']);

function disableEditing() {
    emit('update:modelValue', oldInputValue);
    editMode.value = false;
    emit('decreaseEditingCount');
}

function enableEditing() {
    editMode.value = true;
    emit('increaseEditingCount');
}

const isEditing = computed(() => {
    return props.editingCount && editMode;
})

const showing = ref(true);
</script>