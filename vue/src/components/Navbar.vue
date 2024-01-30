<template>
    <header class="text-2xl shadow-md bg-white py-3">
        <PostCreate v-if="isPostCreateShowed" @close="isPostCreateShowed = false"/>
        <div class="container flex items-center justify-between mx-auto">
            <router-link :to="{ name: 'home' }">
                MyLife
            </router-link>
            <div class="flex items-center">
                <div class="border-neutral-400 border rounded-lg py-1 px-2 flex items-center text-base mr-5 relative">
                    <input @input="searchPeople" v-model="searchInput" type="text" class="outline-none w-[150px]" placeholder="search">
                    <IconSearch />
                    <div v-if="searchedList.length" class="w-32 bg-white absolute rounded-lg translate-y-full -bottom-1 w-full left-0 border p-2">
                        <div v-for="user in searchedList" class="flex items-center">
                            <div class="w-9 h-9 bg-red-400 rounded-full mr-3 mb-1 overflow-hidden">
                                <img :src="user.imageUrl" alt="">
                            </div>
                            <router-link @click="clearSearchInput" :to="{name: 'profile', params: {username: user.username}}"> {{ user.username }}</router-link>
                        </div>
                    </div>
                </div>
                <button @click="isPostCreateShowed = true" class="flex items-center justify-center w-7 h-7 border-2 border-black rounded-[7px] mr-5">
                    <IconPlus />
                </button>
                <div class="relative w-10 h-10">
                    <button ref="openMenuBtn" class="h-10 w-10 rounded-full overflow-hidden"
                        @click="isMenuOpened = !isMenuOpened">
                        <img class="w-10 h-10" :src="user.imageUrl || 'https://i.pinimg.com/originals/8e/d4/04/8ed4040e06e68818a07c3e15ac7c18dc.jpg'"
                            alt="">
                    </button>
                    <!-- Menu -->
                    <div ref="menu" v-show="isMenuOpened" class="h-[200px] w-[180px] absolute top-10 right-0
                    shadow-lg
                    bg-white rounded-lg ">
                        <div class="flex items-center border-gray-300 border-b px-4 py-2">
                            <div class="w-10 h-10 overflow-hidden rounded-full mr-3">
                                <img :src="user.imageUrl || 'https://i.pinimg.com/originals/8e/d4/04/8ed4040e06e68818a07c3e15ac7c18dc.jpg'"
                                    alt="">
                            </div>
                            <div class="text-lg">{{ user.username }}</div>
                        </div>
                        <div class="py-2">
                            <router-link :to="{ name: 'profile', params: {username: authStore.username} }" @click="isMenuOpened = false"
                                class="flex items-center px-4 hover:bg-gray-300">
                                <IconProfile />
                                <span class="text-lg ml-2">Profile</span>
                            </router-link>
                            <button class="flex items-center px-4 hover:bg-gray-300 w-full" @click="logout">
                                <IconExit />
                                <span class="text-lg ml-2">Log out</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { axiosClient } from "../axiosClient";
import IconProfile from "./icons/IconProfile.vue";
import IconExit from "./icons/IconExit.vue";
import PostCreate from "@/views/post/PostCreate.vue"
import IconPlus from "./icons/IconPlus.vue"
import { useAuthStore } from "../stores/auth";
import IconSearch from "./icons/IconSearch.vue";


const user = ref('');
const authStore = useAuthStore();
const isMenuOpened = ref(false);
const openMenuBtn = ref(null);
const menu = ref(null);
let searchedList = ref([])
let searchInput = ref('');
let timer = null;
let isPostCreateShowed = ref(false);

document.addEventListener('click', function x(e) {
    if (isMenuOpened.value && !openMenuBtn.value.contains(e.target) && !menu.value.contains(e.target)) {
        isMenuOpened.value = false;
    }
});

function clearSearchInput() {
    searchedList.value = [];
    searchInput.value = '';
}

function logout() {
    authStore.logout();
}

function searchPeople() {
    if (timer) {
        clearTimeout(timer);
        timer = null;
    }

    timer = setTimeout(async () => {

        if (searchInput.value != '') {
            const response = await axiosClient.get('search', { 
                params: {
                     u: searchInput.value 
                }
            });
            searchedList.value = response.data;
        } else {
            searchedList.value = [];
        }
    }, 400);
}

onMounted(async () => {
    const response = await axiosClient.get(authStore.username);

    user.value = response.data;
});
</script>