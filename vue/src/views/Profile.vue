<template>
    <div class="container mx-auto shadow bg-white  rounded
    mt-3 py-5 
    ">
    <PostDetails v-if="isPostDetailsShowed" :postId="postId" :isPostDetailsShowed="isPostDetailsShowed" @close="isPostDetailsShowed = false"/>
    <!-- <PeopleList :title="'Following'" :url="$route.params.username" /> -->
    <router-view></router-view>
        <div class="flex">
            <div class="h-32 w-32 bg-black rounded-full overflow-hidden cursor-pointer relative group">
                <div class="h-full w-full absolute bg-[#00000070] rounded-full hidden items-center justify-center group-hover:flex"
                    @click="inputFile.click">
                    <IconEdit class="fill-white" />
                </div>
                <img :src="newUser.imageUrl || 'https://i.pinimg.com/originals/8e/d4/04/8ed4040e06e68818a07c3e15ac7c18dc.jpg'"
                    alt="" class="object-cover h-full w-auto">
            </div>
            <div class="h-full ml-14 py-4">
                <span class="block mb-4 text-3xl">{{ newUser.username }}</span>
                <div class="flex gap-12 text-lg">
                    <span><span class="font-semibold mr-2">{{ user.postAmount }}</span>posts</span>
                    <router-link :to="{name: 'followersList'}"><span class="font-semibold mr-2">{{ user.followersAmount }}</span>followers</router-link>
                    <router-link :to="{name: 'followingList'}"><span class="font-semibold mr-2">{{ user.followingAmount }}</span>following</router-link>
                </div>
                <input ref="inputFile" class="hidden" type="file" name="" id="" @change="changeFile" accept="image/*">
                <div v-if="isEditing">
                    <button @click="editingCount = 0"
                        class="hover:bg-rose-600 mr-5 bg-rose-500  mt-16 transition text-white rounded p-2 px-3">Cancel</button>
                    <button @click="updateUser"
                        class="bg-indigo-500 hover:bg-indigo-600  mt-16 transition text-white rounded p-2 px-3">Save</button>
                </div>
            </div>
        </div>
        <!-- posts -->
        <div v-if="posts.length" class="flex grid grid-cols-3 gap-4 mt-10">
            <div v-for="post in posts" :key="post.id" class="group relative cursor-pointer" @click="() => openPostDetails(post.id)">
                <div class="h-full w-auto">
                    <img class="object-cover h-[394px] w-auto" :src="post.mediaUrl" alt="">
                </div>
                <div class="absolute group-hover:block hidden top-0 left-0 bg-[rgba(0,0,0,0.5)] h-full w-full">
                    <div class="flex gap-4 justify-center items-center h-full w-full text-white fill-white stroke-0">
                        <div v-if="post.likesAmount" class="flex"><IconHeart :class="{'fill-red-700': post.isLiked}"/><span class="ml-1">{{ post.likesAmount }}</span></div>
                        <div class="flex"><IconBubble /><span class="ml-1">{{ post.commentsAmount }}</span></div>
                    </div>
                </div>
            </div>
        </div>
        <div v-else class="text-center text-2xl font-semibold my-4">No posts yet</div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useAuthStore } from '@/stores/auth.js';
import { useRoute } from 'vue-router';
import IconEdit from '../components/icons/IconEdit.vue';
import IconHeart from '../components/icons/IconHeart.vue';
import IconBubble from '../components/icons/IconBubble.vue';
import { axiosClient } from '@/axiosClient.js';
import PostDetails from './post/PostDetails.vue';
import PeopleList from '@/components/PeopleList.vue';

let editingCount = ref(0);
const inputFile = ref(null);
const authStore = useAuthStore();
const route = useRoute();
let isEditing = computed(() => !!editingCount.value);
let locked = ref(false);
let user = ref({})
let isPostDetailsShowed = ref(false);
let postId = ref('');
let newUser = ref({});
let posts = ref([]);

function openPostDetails(id) {
    isPostDetailsShowed.value = true;
    postId.value = id;
}

function updateUser() {
    let form = new FormData();
    if (isNewImageAdded.value) form.append('image', inputFile.value.files[0]);

    authStore.updateUser(form);
}

let isNewImageAdded = computed(() => user.value.imageUrl != newUser.value.imageUrl);

function changeFile() {
    const image = inputFile.value.files[0];
    if (!image) {
        URL.revokeObjectURL(newUser.value.imageUrl);
        newUser.value.imageUrl = user.value.imageUrl;
        editingCount.value--;
        locked.value = false;

        return
    }

    const url = URL.createObjectURL(image);
    newUser.value.imageUrl = url;
    if (isNewImageAdded.value && !locked.value) {
        editingCount.value++;
        locked.value = true;
    }
}

onMounted(async () => {
    const response = await axiosClient.get(route.params.username);
    const postsRes = await axiosClient.get(`${route.params.username}/posts`);

    posts.value = postsRes.data;
    user.value = response.data;
    newUser.value = Object.assign({}, user.value);
})

watch(isEditing, () => {
    if (isEditing.value == 0) {
        URL.revokeObjectURL(newUser.value.imageUrl);
        newUser.value.imageUrl = user.value.imageUrl;
    }
})

watch(() => route.params.username, async () => {
    if (route.params.username) {
        const response = await axiosClient.get(route.params.username);
        const postsRes = await axiosClient.get(`${route.params.username}/posts`);

        posts.value = postsRes.data;
        user.value = response.data;
        newUser.value = Object.assign({}, user.value);
    }
})

</script>