import { createRouter, createWebHistory } from "vue-router";
import DefaultLayout from "@/components/DefaultLayout.vue";
import PeopleList from "@/components/PeopleList.vue";
import Login from "@/views/Login.vue";
import Signup from "@/views/Signup.vue";
import Profile from "@/views/Profile.vue";
import Home from "@/views/Home.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      component: DefaultLayout,
      children: [
        {
          path: "/",
          name: "home",
          component: Home,
        },
        {
          path: "/:username",
          name: "profile",
          component: Profile,
          children: [
            {
              path: "following",
              name: "followingList",
              component: PeopleList,
              props: route => ({
                title: "Following",
                url: route.params.username + "/following"
              })
            },
            {
              path: "followers",
              name: "followersList",
              component: PeopleList,
              props: route => ({
                title: "Followers",
                url: route.params.username + "/followers"
              })
            }
          ]
        },
      ],
    },
    {
      path: "/signup",
      name: "signup",
      component: Signup,
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
  ],
});

router.beforeEach(async (to, from) => {
    if (localStorage.secret == null && to.name != 'login' && to.name != 'signup') {
      return { name : 'login'}
    }
});

export default router;
