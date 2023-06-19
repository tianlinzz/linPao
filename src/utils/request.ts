import axios, {AxiosInstance, AxiosRequestConfig, AxiosResponse} from 'axios';
import {ResponseData} from "@/types";
import {showToast} from 'vant';

// 创建Axios实例
const instance: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000, // 设置请求超时时间
});

// 请求拦截器

instance.interceptors.request.use(
    // @ts-ignore
    (config: AxiosRequestConfig) => {
        // 在发送请求之前做些什么，比如添加请求头
        const token = localStorage.getItem('token') as string;
        if (token) {
            // @ts-ignore
            config.headers['Authorization'] = token;
        }
        // 设置JSON格式的请求体
        // @ts-ignore
        config.headers['Content-Type'] = 'application/json;charset=UTF-8';
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
        showToast(res.description || '请求失败');
        return Promise.reject(res.description || '请求失败');
    },
    (error: any) => {
        // 对响应错误做些什么
        showToast(error.message || '请求失败');
        return Promise.reject(error);
    }
);

export default instance;