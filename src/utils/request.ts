import axios, {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios';
import {ResponseData} from "@/types";
import {showToast, showLoadingToast} from 'vant';


let loadingToast: any = null;

const instance: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000, // 设置请求超时时间
    withCredentials: true,
});


// 请求拦截器

instance.interceptors.request.use(
    // @ts-ignore
    (config: AxiosRequestConfig) => {
        loadingToast = showLoadingToast({
            message: '加载中...',
            forbidClick: true,
            duration: 0,
            overlay: true,
        })
        return config;
    },
    (error: any) => {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

// 响应拦截器
instance.interceptors.response.use(
    (response: AxiosResponse) => {
        loadingToast.close();
        const res = response.data as ResponseData;
        if (res.code === 200) {
            return response.data || []
        }
        if (res.code === 40100) {
            // 未登录
            showToast('请先登录');
            window.location.href = '/login';
            return Promise.reject('请先登录');
        }
        showToast(res.description || res.msg || '请求失败');
        return Promise.reject(res.description || res.msg || '请求失败');
    },
    (error: any) => {
        // 对响应错误做些什么
        showToast(error.message || '请求失败');
        return Promise.reject(error);
    }
);

export default instance;