# RxLife
[![](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html) 
[ ![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat-square) ](https://developer.android.com/about/versions/android-4.0.html)
[ ![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square) ](http://www.apache.org/licenses/LICENSE-2.0)
## 利用Jetpack下的Lifecycle获取Activity/Fragment的生命周期变化，通过 BehaviorSubject 转化成 Observable，从而通过Observable.takeUnit(otherObserable) 方法自动完成Observable与界面命周期绑定，公司项目在该库的管控下，RxJava相关导致的内存泄漏率为0%(LeakCanary检测)，强烈推荐使用。

### 为什么重复造轮子？
### 使用RxJava的开发者，一定知道[trello/RxLifecycle](https://github.com/trello/RxLifecycle)项目，本项目LifecycleTransformer也是参考了本项目，但是个人认为trello/RxLifecycle不够简洁，需要继承等等，所以在去年本人就开发了[dhhAndroid/RxLifecycle](https://github.com/dhhAndroid/RxLifecycle)，更加简洁地管理RxJava的生命周期，随着Google推出Jetpack，我发现在dhhAndroid/RxLifecycle库中获取界面生命周期的方式(通过在界面中注入空Fragment)，在JetPack中的Lifecycle模块中已经实现，所以使用Kotlin开发出本库。

# 安装 #
### 由于本库依赖于Jetpack，所以android.support库要在v7:26+以上，例如：com.android.support:appcompat-v7:28.0.0 ###
### RxLife1对应RxJava1版本，RxLife2对应RxJava2版本 ###
#### version：RxLife1：[ ![Download](https://api.bintray.com/packages/dhhandroid/maven/RxLife1/images/download.svg) ](https://bintray.com/dhhandroid/maven/RxLife1/_latestVersion)
```groovy

    //support
    implementation 'com.android.support:appcompat-v7:28.0.0'
    //RxJava1
    implementation 'io.reactivex:rxjava:1.3.8'
    //Rxlife1
    implementation 'com.dhh:rxLife1:version'
    
```
#### version：RxLife2:[ ![Download](https://api.bintray.com/packages/dhhandroid/maven/RxLife2/images/download.svg) ](https://bintray.com/dhhandroid/maven/RxLife2/_latestVersion)
```groovy

    //support
    implementation 'com.android.support:appcompat-v7:28.0.0'
    //RxJava2
    implementation 'io.reactivex.rxjava2:rxjava:2.2.3'
    //Rxlife2
    implementation 'com.dhh:rxLife2:version'
    
```
## 使用
### 前置条件：因support包中的 AppCompatActivity 和 android.support.v4.app.Fragment 均已实现 LifecycleOwner，所以确保你的项目 BaseActivity 继承于 AppCompatActivity，BaseFragment继承于 android.support.v4.app.Fragment。 ###
### RxLife1与RxLife2使用方式完全一样，以RxJava2版本为例：
### 标准用法，使用RxLife.with(lifecycleOwner),以下compose在一个Observable事件流上使用一种即可： ###
```kotlin

        Observable.timer(10, TimeUnit.SECONDS)
                //标准使用模式,自动在[Lifecycle.Event.ON_DESTROY]注销
                .compose(RxLife.with(lifecycleOwner).bindToLifecycle())
                //指定在[Lifecycle.Event.ON_DESTROY]注销
                .compose(RxLife.with(lifecycleOwner).bindOnDestroy())
                //指定在某一生命周期注销，不常用
                .compose(RxLife.with(lifecycleOwner).bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe { Log.d("RxLife2-onCreate", it.toString()) }
```
### 通过Kotlin扩展函数使用，Kotlin项目推荐使用： ###
```kotlin

        Observable.timer(10, TimeUnit.SECONDS)
                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_STOP]注销
                .bindToLifecycle(lifecycleOwner)
                //指定在[Lifecycle.Event.ON_DESTROY]注销
                .bindonDestroy(lifecycleOwner)
                //指定在某一生命周期注销，不常用
                .bindUntilEvent(lifecycleOwner, Lifecycle.Event.ON_DESTROY)
                .subscribe { Log.d("RxLife2-onCreate", it.toString()) }
```
## 在MVP中使用，其实就是在使用的地方，能提供获取lifecycleOwner的方法即可，demo中有一个简易的MVP demo，详情查看demo。 ##

