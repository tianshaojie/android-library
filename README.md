# Android-Library

## Base UI
1. BaseActivity，BaseFragment
    * 沉浸模式 （4.X，键盘冲突，主流机型）
    * Toolbar 统一样式
    * Vector 图标兼容
2. Utils，基于[Blankj/AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)，有删减和添加；

## Http：OkHttp3、Retrofit2、RxJava2
1. 封装Response数据结构：BaseObserver处理服务器响应
2. 统一预处理请求：header，cache
3. 统一异常处理：特定错误提示，token失效重新登录
4. 统一加载动画ProgressDialog
5. 自动取消Http请求：使用RxLifecycle自动取消请求
6. 请求失败后的Retry封装处理
7. RxLifecycle管理生命周期，防止泄露：集成RxActivity, RxFragment
8. Cookie实现免登录
9. Rx转换类，简化请求调用

## Other
* 常用效率库；

## PS
* Demo 逐步完善中；其他基础库陆续开发中；
* 项目中同步使用该Library
* 问题及时反馈
* 项目开发首选；