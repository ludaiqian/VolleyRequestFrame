# RequestFrameSample


# 项目描述

基于Volley的基础框架

## 请求框架如何使用：

* 对接口协议进行抽象，编写具体的请求和响应对象分别继承Request,Response
* 在ZitechApi里面注册一下接口名称对应的返回类型
* 在ApiFactory里添加这个接口的方法

## 下载模块的使用：

1、在Manifest添加下载相关配置,其中有三个自定义权限

 <!-- manifest 节点下添加 -->
    <permission
        android:name="com.mozillaonline.permission.ACCESS_DOWNLOAD_MANAGER"
        android:description="@string/permdesc_downloadManager"
        android:label="@string/permlab_downloadManager"
        android:protectionLevel="normal" />

    <!-- Allows advanced access to the Download Manager -->
    <permission
        android:name="com.mozillaonline.permission.ACCESS_DOWNLOAD_MANAGER_ADVANCED"
        android:description="@string/permdesc_downloadManagerAdvanced"
        android:label="@string/permlab_downloadManagerAdvanced"
        android:protectionLevel="normal" />

    <!-- Allows to send broadcasts on download completion -->
    <permission
        android:name="com.mozillaonline.permission.SEND_DOWNLOAD_COMPLETED_INTENTS"
        android:description="@string/permdesc_downloadCompletedIntent"
        android:label="@string/permlab_downloadCompletedIntent"
        android:protectionLevel="normal" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="com.mozillaonline.permission.ACCESS_DOWNLOAD_MANAGER" />
    <uses-permission android:name="com.mozillaonline.permission.ACCESS_DOWNLOAD_MANAGER_ADVANCED" />
    <uses-permission android:name="com.mozillaonline.permission.SEND_DOWNLOAD_COMPLETED_INTENTS" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />


        <!-- Application节点下添加 -->
        <activity android:name="com.mozillaonline.providers.downloads.SizeLimitActivity" />

        <service android:name="com.mozillaonline.providers.downloads.DownloadService" />

        <provider
            android:name="com.mozillaonline.providers.downloads.DownloadProvider"
            android:authorities="com.mozillaonline.downloads"
            android:exported="false" />

        <receiver
            android:name="com.mozillaonline.providers.downloads.DownloadReceiver"
            android:exported="false" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
            </intent-filter>
        </receiver>
2、创建DownloadManager

3、创建DownloadManager.Request

4、添加到下载队列manager.enqueue(request);

5、下载进度监听使用ContentObserver
