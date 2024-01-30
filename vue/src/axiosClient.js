import axios from "axios";
import { useAuthStore } from "./stores/auth";

export const axiosClient = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    // paramsSerializer: {
    //     encode: parse,
    //     serialize: stringify,
    //   },
});

axiosClient.interceptors.request.use(config => {
    const authStore = useAuthStore();

    if (authStore.secret != null) {
        config.headers.Authorization = "Basic " + authStore.secret;
    }
    
    return config;
});