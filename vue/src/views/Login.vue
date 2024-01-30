<template>
    <div>
        <form @submit.prevent="login" class="flex flex-col items-center w-[300px] mx-auto mt-[132px]
         gap-8 bg-white shadow rounded p-12">
            <h1 class="text-center text-gray-600 text-xl">Login page</h1>
            <div class="flex flex-col">
                <label for="" class="mb-1 text-gray-700">Username</label>
                <input type="text" class="rounded outline-indigo-400 border-gray-400 border p-1 px-2"
                    v-model="user.username">
                <label for="" class="mb-1 mt-4 text-gray-700">Password</label>
                <input type="text" class="rounded outline-indigo-400 border-gray-400 border p-1 px-2"
                    v-model="user.password">
            </div>
            <div class="w-full flex justify-between items-center">
                <router-link :to="{name: 'signup'}" class="hover:underline hover:text-indigo-700 underline-offset-2 transition">Sign up</router-link>
                <button type="submit" value="submit" class="bg-indigo-400 rounded text-white hover:bg-indigo-500 p-1 px-8
            active:scale-[0.99] transition-all
            ">Log in</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { reactive } from "vue";
import { useAuthStore } from "@/stores/auth.js"
import {useRouter} from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();
let user = reactive({
    username: '',
    password: ''
})

function login() {
    let status = authStore.login(user.username, user.password);
    
    if (status) {
        router.push({name: 'home'})
    }
}
</script>