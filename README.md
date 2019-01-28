# RxLife
[![](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html) 
[ ![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat-square) ](https://developer.android.com/about/versions/android-4.0.html)
[ ![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square) ](http://www.apache.org/licenses/LICENSE-2.0)
## 利用 Jetpack 下的 Lifecycle 获取 Activity/Fragment 的生命周期变化，通过 BehaviorSubject 转化成 Observable，从而通过 Observable.takeUnit(otherObserable) 方法自动完成 Observable 与界面命周期绑定，在日常开发过程中，在该库的管控下，RxJava 相关导致的内存泄漏率为 0%(LeakCanary 检测)，强烈推荐使用。

### 为什么重复造轮子？
### 使用 RxJava 的开发者，一定知道 [trello/RxLifecycle](https://github.com/trello/RxLifecycle) 项目，本项目 LifecycleTransformer 也是参考了 trello/RxLifecycle，但是个人认为 trello/RxLifecycle 不够简洁，需要继承等等，所以在去年本人就开发了 [dhhAndroid/RxLifecycle](https://github.com/dhhAndroid/RxLifecycle)，更加简洁地管理 RxJava 的生命周期；随着 Google 推出 Jetpack，我发现在 dhhAndroid/RxLifecycle 库中获取界面生命周期的方式 (通过在界面中注入空 Fragment)，在 JetPack 中的 Lifecycle 模块中已经实现，所以使用 Kotlin 结合 Jetpack 开发出了本项目。

# 安装 #
### 由于本库依赖于 Jetpack，所以 android.support 库要在 v7:26.1.x 以上，例如：com.android.support:appcompat-v7:28.0.0 ###
### RxLife1 对应 RxJava1 版本，RxLife2 对应 RxJava2 版本 ###
#### version：RxLife1：[ ![Download](https://api.bintray.com/packages/dhhandroid/maven/RxLife1/images/download.svg) ](https://bintray.com/dhhandroid/maven/RxLife1/_latestVersion)
```groovy

    //support
    implementation 'com.android.support:appcompat-v7:28.0.0' //27.0.0 等
    //RxJava1
    implementation 'io.reactivex:rxjava:1.3.8'
    //Rxlife1
    implementation 'com.dhh:rxLife1:version'
    
```
#### version：RxLife2:[ ![Download](https://api.bintray.com/packages/dhhandroid/maven/RxLife2/images/download.svg) ](https://bintray.com/dhhandroid/maven/RxLife2/_latestVersion)
```groovy

    //support
    implementation 'com.android.support:appcompat-v7:28.0.0' //27.0.0 等
    //RxJava2
    implementation 'io.reactivex.rxjava2:rxjava:2.2.3'
    //Rxlife2
    implementation 'com.dhh:rxLife2:version'
    
```
## 使用（[查看 demo](https://github.com/dhhAndroid/RxLife/tree/master/demo)）
### 前置条件：因 support 包中的 AppCompatActivity 和 android.support.v4.app.Fragment 均已实现 LifecycleOwner，所以确保你的项目 BaseActivity 继承于 AppCompatActivity，BaseFragment 继承于 android.support.v4.app.Fragment。 ###
### RxLife1 与 RxLife2 使用方式完全一样，以 RxJava2 版本为例：
### 标准用法（Java 或 Kotlin 项目），使用 RxLife.with(lifecycleOwner), 以下 compose 在一个 Observable 事件流上使用一种即可： ###
```kotlin

        Observable.timer(10, TimeUnit.SECONDS)
                // 标准使用模式 , 自动在 [Lifecycle.Event.ON_DESTROY] 注销
                .compose(RxLife.with(lifecycleOwner).bindToLifecycle())
                // 指定在 [Lifecycle.Event.ON_DESTROY] 注销
                .compose(RxLife.with(lifecycleOwner).bindOnDestroy())
                // 指定在某一生命周期注销，不常用
                .compose(RxLife.with(lifecycleOwner).bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe { Log.d("RxLife2-onCreate", it.toString()) }
```
### 通过 Kotlin 扩展函数使用，Kotlin 项目推荐使用： ###
```kotlin

        Observable.timer(10, TimeUnit.SECONDS)
                // 通过 kotlin 扩展方法使用，推荐；自动在 [Lifecycle.Event.ON_STOP] 注销
                .bindToLifecycle(lifecycleOwner)
                // 指定在 [Lifecycle.Event.ON_DESTROY] 注销
                .bindOnDestroy(lifecycleOwner)
                // 指定在某一生命周期注销，不常用
                .bindUntilEvent(lifecycleOwner, Lifecycle.Event.ON_DESTROY)
                .subscribe { Log.d("RxLife2-onCreate", it.toString()) }
```
## 在 MVP 中使用，其实就是在使用的地方，能提供获取 lifecycleOwner 的方法即可，demo 中有一个简易的 MVP demo，详情查看 demo。 ##

## License
```
   Copyright 2019 dhhAndroid

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
