# LC-SDK 适用于获取力扣相关信息的SDK

基于力扣网站上的请求,封装的SDK。使用此SDK需遵循网站的协议。

* [LeetCode Terms of Service](https://leetcode.com/terms/)
* [力扣（LeetCode）• 使用条例](https://leetcode.cn/terms-c)

## 最低需求

Java 1.8或更高版本:

## 使用maven

```xml
<dependency>
    <groupId>com.shuzijun</groupId>
    <artifactId>lc-sdk</artifactId>
    <version>0.0.4</version>
</dependency>
```

## 使用gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.shuzijun:lc-sdk:0.0.4")
}
```

如果需要使用 GitHub Packages，需要额外配置 `https://maven.pkg.github.com/shuzijun/lc-sdk` 仓库和具备 `read:packages` 权限的凭据。公开依赖建议优先使用 Maven Central。

## 直接下载JAR

你可以到maven仓库直接下载最新版的[JAR](https://github.com/shuzijun/lc-sdk/releases)。

## 快速入门示例

参考[LcClientTest](./src/test/java/com/shuzijun/lc/LcClientTest.java)

## License 授权协议

这个项目 GPL 协议， 请点击 [LICENSE](./LICENSE) 了解更多细节。
