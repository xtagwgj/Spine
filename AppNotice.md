# [Android应用优化方案](https://juejin.im/post/5a26919b51882533d022ed04)

## 启动优化

 添加一个闪屏的页面，用于品牌的展示

- 定义一个Style：
 ```java
    <style name="AppTheme.Launcher">
        <item name="android:windowBackground">@drawable/branded_launch_screens</item>
    </style>
 ```

 - drawable/branded_launch_screens
 ```java
    <?xml version="1.0" encoding="utf-8"?>
    <layer-list     
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:opacity="opaque">

        <!--黑色背景颜色-->
        <item android:drawable="@android:color/black" />

        <!-- 产品logo-->
        <item>
            <bitmap
                android:gravity="center"
                android:src="@mipmap/empty_image01" />
        </item>

        <!-- 右上角的图标元素 -->
        <item>
            <bitmap
                android:gravity="top|right"
                android:src="@mipmap/github" />
        </item>

        <!--最下面的文字-->
        <item android:bottom="50dp">
            <bitmap
                android:gravity="bottom"
                android:src="@mipmap/ic_launcher" />
        </item>
    </layer-list>
 ```

 其中android:opacity=”opaque”参数是为了防止在启动的时候出现背景的闪烁。或者直接使用图片替代

 ```java
 <style name="AppTheme.Splash">
    <item name="android:windowFullscreen">true</item>
    <item name="android:windowBackground">@mipmap/app_welcome</item>
</style>
 ```