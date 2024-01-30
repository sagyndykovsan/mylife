import { defineStore } from "pinia";
import { axiosClient } from "../axiosClient";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    username: localStorage.getItem("username") || null,
    password: localStorage.getItem("password") || null,
    secret: localStorage.getItem("secret") || null,
    // username: 'cdawg',
    // password: '123',
    // secret: btoa(username, password)
  }),
  actions: {
    async login(username, password) {
        const response = await axiosClient.get('login', {
            headers: {
              Authorization: "Basic " + btoa(`${username}:${password}`)
            }
        })

      if (response.status == 200) {
        console.log("LOGGED IN");

        localStorage.setItem("username", username);
        localStorage.setItem("password", password);
        localStorage.setItem("secret", btoa(`${username}:${password}`));

        this.username = username;
        this.password = password;
        this.secret = btoa(`${username}:${password}`);
      }

      return true;
    },
    async register(username, password) {
      const response = await axiosClient.post('register', {username, password});

      if (response.status == 200) {
        console.log(response.data);

        localStorage.setItem("username", username);
        localStorage.setItem("password", password);
        localStorage.setItem("secret", btoa(`${username}:${password}`));

        this.username = username;
        this.password = password;
        this.secret = btoa(`${username}:${password}`);
      }
    },
    async updateUser(form) {
      const response = await axiosClient.patch('profile', form);
      
      console.log(response);
    },
    async logout() {
      const response = await axiosClient.post('logout');
      
      if (response.status == 200) {
        localStorage.clear();
        this.username = null;
        this.password = null;
        this.secret = null;
      }
    }
  },
});