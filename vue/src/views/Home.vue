<template>
    <div class="mt-8 mx-auto w-fit border-black">
        <PostDetails v-if="isPostDetailsShowed" :postId="postId" :isPostDetailsShowed="isPostDetailsShowed" @close="isPostDetailsShowed = false"/>
        <div class="border-b border-gray-400 pb-6 mb-5" v-for="post in posts" :key="post.id">
            <div class="flex items-center mb-2">
                <router-link :to="{name: 'profile', params: {username: post.username}}" class="w-10 h-10 bg-black rounded-full overflow-hidden">
                    <img :src="post.userImageUrl" alt="" class="object-cover">
                </router-link>
                <router-link :to="{name: 'profile', params: {username: post.username}}" class="ml-3 text-md font-semibold">{{ post.username }}</router-link>
            </div>
            <!-- Post image -->
            <div class="w-[400px] h-[450px] border-black">
                <img class="object-cover h-full"
                    :src="post.mediaUrl"
                    alt="">
            </div>
            <div class="flex mt-2 gap-4">
                <button @click="() => likePost(post.id)">
                    <IconHeart class="cursor-pointer" :class="post.isLiked ? 'fill-red-700' : 'fill-none'" />
                </button>
                <button @click="() => openPostDetails(post.id)" >
                    <IconBubble class="cursor-pointer fill-none" />
                </button>
            </div>
            <div class="mb-1">{{ post.likesAmount }} likes</div>
            <div>
                <span class="font-semibold">{{ post.username }}</span>
                {{ post.description }}
            </div>
            <!-- comments -->
            <div class="w-fit mt-1 cursor-pointer" @click="() => openPostDetails(post.id)">View all {{post.commentsAmount}} comments</div>
        </div>
        <div ref="end"></div>
    </div>
</template>

<script setup>
import IconHeart from '../components/icons/IconHeart.vue';
import IconBubble from '../components/icons/IconBubble.vue';
import { axiosClient } from '../axiosClient';
import { ref, onMounted, computed } from 'vue';
import PostDetails from './post/PostDetails.vue';

const posts = ref([]);
const postId = ref('');
const end = ref(null);
const isPostDetailsShowed = ref(false);
let isLoading = false;
let isLastPage = false;
let page = 0;

function openPostDetails(id) {
    isPostDetailsShowed.value = true;
    postId.value = id;
}

async function likePost(postId) {
    const response = await axiosClient.post(`posts/${postId}/like`);
    
    if (response.status == 200) {
        let post = posts.value.find(post => post.id == postId);
        
        if (post.isLiked) {
            post.likesAmount--;
            post.isLiked = false;
        } else {
            post.likesAmount++;
            post.isLiked = true;
        }

    }
}

async function getPosts() {
    if (isLoading == false && !isLastPage) {
        isLoading = true;
        const response = await axiosClient.get("posts", {
            params: {
                page: page++
            }
        })
        posts.value = posts.value.concat(response.data.content);
        isLastPage = response.data.last;
        isLoading = false;
    }
    
}

onMounted(() => {
    getPosts()

    let opt = {
        root: null,
        rootMargin: '100px',
        treshold: 0
    }
    function callback(entries, observer) {
        if(entries[0].isIntersecting) {
            getPosts()
        }
    }

    let observer = new IntersectionObserver(callback, opt);
    observer.observe(end.value)
})

</script>