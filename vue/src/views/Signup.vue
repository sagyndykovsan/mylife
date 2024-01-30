<template>
    <div>
        <form @submit.prevent="login" class="flex flex-col items-center w-[300px] mx-auto mt-[132px]
         gap-8 bg-white shadow rounded p-12">
            <h1 class="text-center text-gray-600 text-xl">Sign up page</h1>
            <div class="flex flex-col">
                <label for="" class="mb-1 text-gray-700">Username</label>
                <input type="text" class="rounded outline-indigo-400 border-gray-400 border p-1 px-2"
                    v-model="user.username">
                <label for="" class="mb-1 mt-4 text-gray-700">Password</label>
                <input type="text" class="rounded outline-indigo-400 border-gray-400 border p-1 px-2"
                    v-model="user.password">
            </div>
            <div class="w-full justify-center flex">
                <button type="submit" value="submit" class="bg-indigo-400 rounded text-white hover:bg-indigo-500 p-1 px-12
            active:scale-[0.99] transition-all
            ">Sign Up</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { reactive } from "vue";
import { useAuthStore } from "@/stores/auth.js"

const authStore = useAuthStore();
let user = reactive({
    username: '',
    password: ''
})

function login() {
    authStore.register(user.username, user.password);
}
</script>