<template>
    <div ref="blackout" class="w-full h-full bg-[rgb(0,0,0,75%)] items-center justify-center flex
        fixed
        z-30
        top-0
        left-0
    ">
        <button @click="$emit('close')" class="absolute top-3 right-3">
            <IconClose />
        </button>
        <div class="flex rounded overflow-hidden" v-if="post">
            <div class="w-[600px] h-[600px] bg-black">
                <img :src="post.mediaUrl" alt="" class="object-cover h-full">
            </div>
            <div class="bg-white min-w-[300px] max-h-[600px] flex flex-col">
                <!-- post author -->
                <div class="flex items-center p-3 border-b border-gray-300">
                    <div class="w-10 h-10 rounded-full overflow-hidden">
                        <img :src="post.userImageUrl || 'https://i.pinimg.com/originals/8e/d4/04/8ed4040e06e68818a07c3e15ac7c18dc.jpg'"
                            alt="">
                    </div>
                    <span class="ml-3">{{ post.username }}</span>
                </div>
                <!-- post author end -->
                <div class="max-h-full overflow-auto">
                    <!-- post description -->
                    <div class="flex p-3">
                        <div class="w-10 h-10 rounded-full overflow-hidden">
                            <img :src="post.userImageUrl || 'https://i.pinimg.com/originals/8e/d4/04/8ed4040e06e68818a07c3e15ac7c18dc.jpg'"
                                alt="">
                        </div>
                        <div class="ml-3">
                            <div>
                                <span class="font-semibold mr-1.5">{{ post.username }}</span>
                                <span>{{ post.description }}</span>
                            </div>
                            <div class="text-sm text-gray-500">
                                {{ getFormatedTimeDiff(post.createdAt) }}
                            </div>
                        </div>
                    </div>
                    <!-- post description end -->
                    <!-- comments -->
                    <div class="flex p-3" v-for="comment in comments">
                        <div class="w-10 h-10 rounded-full overflow-hidden shrink-0">
                            <img :src="comment.userImageUrl || 'https://i.pinimg.com/originals/8e/d4/04/8ed4040e06e68818a07c3e15ac7c18dc.jpg'"
                                alt="">
                        </div>
                        <div class="ml-3 max-w-[260px]">
                            <div class="w-full">
                                <span class="font-semibold mr-1.5">{{ comment.username }}</span>
                                <span class="break-words">{{ comment.text }}</span>
                            </div>
                            <div class="text-sm text-gray-500">
                                {{ getFormatedTimeDiff(comment.createdAt) }}
                            </div>
                        </div>
                    </div>
                    <!-- comments end -->
                </div>
                <!-- write comment -->
                <div class="mt-auto">
                    <div class="border-t border-gray-300 flex flex-col p-2">
                        <div class="flex items-center">
                            <button @click="likePost(post.id)">
                                <IconHeart :class="post.isLiked ? 'fill-red-700':'fill-none'" />
                            </button>
                            <span class="ml-2 font-semibold text-sm"> {{ post.likesAmount }} likes </span>
                        </div>
                        <div class="text-sm text-gray-400">
                            {{ getFormatedTimeDiff(post.createdAt) }}
                            ago
                        </div>
                    </div>
                    <div class="w-full border-t border-gray-300 p-3 pt-3.5 relative">
                        <textarea v-model="commentField" @keypress.enter.exact="sendComment" @input="updateStylesOfTextArea"
                            placeholder="write a comment" name="comment" id="comment"
                            class="max-h-[96px] h-[24px] w-full outline-none resize-none overflow-hidden box-border"></textarea>
                        <div class="p-3 opacity-0 absolute top-0 left-0 -z-10 w-full">
                            <textarea ref="hiddenTextArea"
                                class="bg-red-300 w-full h-[24px] outline-none resize-none overflow-hidden"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue';
import { axiosClient } from '../../axiosClient';
import IconClose from '../../components/icons/IconClose.vue';
import IconHeart from '../../components/icons/IconHeart.vue';

const props = defineProps(['postId']);
const emit = defineEmits(['close']);
const post = ref('');
const comments = ref([]);
const blackout = ref(null);
const hiddenTextArea = ref();
let commentField = ref('');
let counter = ref(0);

function updateStylesOfTextArea(e) {
    const el = e.target;
    hiddenTextArea.value.value = el.value;
    let scrollHeight = hiddenTextArea.value.scrollHeight;
    let cols = scrollHeight / 24;
    let height = (cols > 4 ? 4 : cols) * 24;

    el.style.height = height + 'px';

    if (cols > 4) {
        el.style.overflowY = 'scroll';
        hiddenTextArea.value.style.overflowY = 'scroll';
    } else {
        el.style.overflowY = 'hidden';
        hiddenTextArea.value.style.overflowY = 'hidden';
    }
}

function getFormatedTimeDiff(date) {
    const postTimestamp = new Date(date);
    let now = Date.now();
    const diff = now - postTimestamp;

    let subtractor = now - 1000 * 60;
    if (postTimestamp > subtractor) return Math.trunc(diff / 1000) + "s";
    subtractor = now - 1000 * 3600;
    if (postTimestamp > subtractor) return Math.trunc(diff / 1000 / 60) + "m";
    subtractor = now - 1000 * 3600 * 24;
    if (postTimestamp > subtractor) return Math.trunc(diff / 1000 / 3600) + "h";
    subtractor = now - 1000 * 3600 * 24 * 7;
    if (postTimestamp > subtractor) return Math.trunc(diff / 1000 / 3600 / 24) + "d";

    return Math.trunc(diff / 1000 / 3600 / 24 / 7) + "w";
}

document.addEventListener('click', e => {
    if (e.target == blackout.value) emit('close');
})

async function sendComment(e) {
    e.preventDefault();
    if (commentField.value == "") return;

    const response = await axiosClient.post(`posts/${props.postId}/comments/create`, {
        post: {
            id: props.postId
        },
        text: commentField.value
    });
}

async function likePost(postId) {
    const response = await axiosClient.post(`posts/${postId}/like`);
    
    if (response.status == 200) {
        if (post.value.isLiked) {
            post.value.likesAmount--;
            post.value.isLiked = false;
            console.log('d');
        } else {
            post.value.likesAmount++;
            post.value.isLiked = true;
        }

    }
}

onMounted(async () => {
    const postResponse = await axiosClient.get(`posts/${props.postId}`);
    const commentsReponse = await axiosClient.get(`posts/${props.postId}/comments`);

    post.value = postResponse.data;
    comments.value = commentsReponse.data;
}) 
</script>