<template>
    <div @click.self="goBack" class="w-full h-full bg-[rgb(0,0,0,75%)] items-center justify-center flex
        fixed
        z-30
        top-0
        left-0
    ">
        <div class="flex flex-col rounded-xl text-white overflow-hidden bg-neutral-800">
            <div class="flex justify-between border-b border-neutral-700 relative">
                <h1 class="p-4 mx-auto text-center text-lg font-semibold capitalize border-neutral-700">{{ title }}</h1>
                <button @click="goBack" ref="closeBtn" class="absolute right-4 top-1/2 -translate-y-1/2">
                    <IconClose />
                </button>
            </div>
            <div v-if="peopleList.length" class="p-4 flex flex-col gap-3">
                <!-- user -->
                <div v-for="user in peopleList" class="flex justify-between items-center w-[350px]">
                    <div class="flex items-center">
                        <div class="w-10 h-10 rounded-full bg-red-700 mr-2 overflow-hidden">
                            <img :src="user.imageUrl" alt="">
                        </div>
                        <router-link :to="{name:'profile',params:{username:user.username}}">{{ user.username }}</router-link>
                    </div>
                    <button class="bg-blue-500 px-6 py-1 rounded-lg">follow</button>
                </div>
            </div>
            <div v-else class="w-[350px] text-center py-2 text-neutral-500">
                this person don't follow anyone
            </div>
        </div>
    </div>
</template>

<script setup>
import IconClose from './icons/IconClose.vue';
import { onMounted, watch, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { axiosClient } from '../axiosClient';

const props = defineProps(['title', 'url', 'username']);
const router = useRouter();
const route = useRoute();
const peopleList = ref([]);

function goBack(e) {
        router.go(-1);
}

onMounted(async () => {
    const response = await axiosClient.get(props.url);
    
    peopleList.value = response.data;
});

</script>