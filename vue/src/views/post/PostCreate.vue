<template>
    <div ref="blackout" class="w-full h-full bg-[rgb(0,0,0,75%)] items-center justify-center flex
        fixed
        z-10
        top-0
        left-0
    ">
        <button @click="$emit('close')" class="absolute top-3 right-3">
            <IconClose />
        </button>
        <!-- form window -->
        <div class="rounded-md overflow-hidden">
            <div class="h-9 px-3 w-full bg-neutral-800 border-b border-neutral-700 items-center flex justify-between">
                <button @click="currentStep--" v-if="mediaUrl">
                    <IconArrowLeft />
                </button>
                <div class="text-white text-base mx-auto">
                    <span v-if="currentStep == STEP_PHOTO">Add photo</span>
                    <span v-if="currentStep == STEP_SHARE">Create new post</span>
                </div>
                <div class="flex items-center" v-if="mediaUrl">
                    <button v-if="currentStep == STEP_SHARE" @click="sendPost"
                        class="text-base text-blue-500 font-semibold">
                        Share
                    </button>
                    <button v-else @click="currentStep++" class="text-base text-blue-500 font-semibold">
                        Next
                    </button>
                </div>
            </div>
            <div class="h-[600px] max-w-[900px] bg-neutral-800 flex">
                <div class="w-[600px] h-full">
                    <div v-if="mediaUrl">
                        <img :src="mediaUrl" alt="" class="w-full">
                    </div>
                    <div v-show="!mediaUrl" class="flex items-center justify-center h-full">
                        <button @click="fileField.click" class="bg-blue-500 text-base p-2 py-1 rounded text-white">Select
                            from computer</button>
                        <input ref="fileField" @change="changeFile" class="hidden" type="file" name="" id=""
                            accept="image/*">
                    </div>
                </div>
                <div
                    class="border-l border-neutral-700 transition-[width] ease-linear"
                    :class="currentStep == STEP_SHARE ? 'w-[300px] p-3' : 'w-[0px] p-0'">
                    <textarea v-model="caption" placeholder="Write a caption" class="text-white bg-transparent w-full outline-none text-base resize-none"
                        rows="7"></textarea>
                    <div class="ml-auto text-sm text-neutral-600 w-fit">
                        {{ captionLength }}/2000
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import IconClose from '../../components/icons/IconClose.vue';
import IconArrowLeft from '@/components/icons/IconArrowLeft.vue';
import { axiosClient } from '@/axiosClient.js';

const blackout = ref(null);
const fileField = ref(null);
const emit = defineEmits(['close'])
const STEP_PHOTO = 1;
const STEP_SHARE = 2;

let caption = ref('');
let currentStep = ref(STEP_PHOTO);
let mediaUrl = ref('');
let captionLength = computed(() => caption.value.length);

document.addEventListener('click', e => {
    if (e.target == blackout.value) emit('close');
})

function changeFile() {
    const image = fileField.value.files[0];

    if (!image) {
        const url = URL.revokeObjectURL(mediaUrl.value);
        mediaUrl.value = '';

        return;
    }

    const url = URL.createObjectURL(image);
    mediaUrl.value = url;
}

async function sendPost() {
    let form = new FormData();
    
    const file = fileField.value.files[0];
    
    if (file) {
        form.append('media', file);
    }
    
    if (caption.value) {
        form.append('description', caption.value);
    }

    const response = await axiosClient.post('/posts/create', form);
    
    if (response.status == 200) emit('close');
}
</script>