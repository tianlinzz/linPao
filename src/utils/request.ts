import axios, {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios';
import {ResponseData} from "@/types";
import {showToast} from 'vant';

const instance: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000, // 设置请求超时时间
    withCredentials: true,
});


// 请求拦截器

instance.interceptors.request.use(
    // @ts-ignore
    (config: AxiosRequestConfig) => {
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
        // 对响应数据做些什么
        const res = response.data as ResponseData;
        if (res.code === 200) {
            showToast('请求成功');
            return response.data || []
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