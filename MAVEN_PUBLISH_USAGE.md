# Maven Central 发布配置使用指南

## 文件说明

`maven-publish.gradle` 是一个通用的 Maven Central 发布配置文件，可以在任何 Android 或 Java 项目中复用。

## 使用方法

### 1. 复制文件

将 `maven-publish.gradle` 文件复制到你的项目根目录。

### 2. 在根目录 build.gradle 中添加插件

```gradle
plugins {
    id 'signing' version '1.0.1' apply false
    id 'maven-publish' apply false
}
```

### 3. 在需要发布的模块 build.gradle 中配置

#### Android Library 项目示例：

```gradle
plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.android'
}

// Maven Central 发布配置
ext {
    PUBLISH_GROUP_ID = 'com.yourcompany'
    PUBLISH_ARTIFACT_ID = 'your-library'
    PUBLISH_VERSION = '1.0.0'
    PUBLISH_DESCRIPTION = 'Your library description'
    PUBLISH_URL = 'https://github.com/yourusername/your-repo'
    PUBLISH_LICENSE_NAME = 'The MIT License (MIT)'
    PUBLISH_LICENSE_URL = 'https://raw.githubusercontent.com/yourusername/your-repo/master/LICENSE'
    PUBLISH_DEVELOPER_ID = 'your-id'
    PUBLISH_DEVELOPER_NAME = 'Your Name'
    PUBLISH_DEVELOPER_EMAIL = 'your.email@example.com'
}

group = PUBLISH_GROUP_ID
version = PUBLISH_VERSION

android {
    // ... 你的 Android 配置
}

// 应用 Maven Central 发布配置
apply from: rootProject.file('maven-publish.gradle')
```

#### Java Library 项目示例：

```gradle
plugins {
    id 'java-library'
}

// Maven Central 发布配置
ext {
    PUBLISH_GROUP_ID = 'com.yourcompany'
    PUBLISH_ARTIFACT_ID = 'your-library'
    PUBLISH_VERSION = '1.0.0'
    PUBLISH_DESCRIPTION = 'Your library description'
    PUBLISH_URL = 'https://github.com/yourusername/your-repo'
    PUBLISH_LICENSE_NAME = 'The MIT License (MIT)'
    PUBLISH_LICENSE_URL = 'https://raw.githubusercontent.com/yourusername/your-repo/master/LICENSE'
    PUBLISH_DEVELOPER_ID = 'your-id'
    PUBLISH_DEVELOPER_NAME = 'Your Name'
    PUBLISH_DEVELOPER_EMAIL = 'your.email@example.com'
}

group = PUBLISH_GROUP_ID
version = PUBLISH_VERSION

// 应用 Maven Central 发布配置
apply from: rootProject.file('maven-publish.gradle')
```

### 4. 配置认证信息

在 `local.properties` 文件中添加 Maven Central 认证信息：

```properties
# Maven Central 认证信息
MAVEN_CENTRAL_USERNAME=your_username
MAVEN_CENTRAL_PASSWORD=your_password
```

### 5. 配置 GPG 签名（必需）

Maven Central 要求所有发布的内容必须使用 GPG 签名。

#### 安装 GPG
```bash
# macOS
brew install gnupg

# Linux
sudo apt-get install gnupg

# Windows
# 下载并安装 Gpg4win
```

#### 生成 GPG 密钥
```bash
gpg --gen-key
# 按照提示填写信息
```

#### 导出公钥并上传到密钥服务器
```bash
# 导出公钥
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID

# 或者上传到其他密钥服务器
gpg --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID
```

### 6. 发布

```bash
# 发布到 Maven Central
./gradlew :your-module:publishReleasePublicationToMavenCentralRepository

# 或者发布所有变体
./gradlew :your-module:publish
```

### 7. 在 Sonatype Nexus 中完成发布

1. 访问: https://s01.oss.sonatype.org/
2. 使用 Maven Central 账号登录
3. 进入 "Staging Repositories"
4. 找到你的 staging repository
5. 点击 "Close" 按钮（验证通过后）
6. 点击 "Release" 按钮发布到 Maven Central

## 配置参数说明

| 参数 | 说明 | 必需 |
|------|------|------|
| `PUBLISH_GROUP_ID` | Maven Group ID | ✅ |
| `PUBLISH_ARTIFACT_ID` | Maven Artifact ID | ✅ |
| `PUBLISH_VERSION` | 版本号 | ✅ |
| `PUBLISH_DESCRIPTION` | 项目描述 | ❌ |
| `PUBLISH_URL` | 项目 URL | ❌ |
| `PUBLISH_LICENSE_NAME` | 许可证名称 | ❌ |
| `PUBLISH_LICENSE_URL` | 许可证 URL | ❌ |
| `PUBLISH_DEVELOPER_ID` | 开发者 ID | ❌ |
| `PUBLISH_DEVELOPER_NAME` | 开发者名称 | ❌ |
| `PUBLISH_DEVELOPER_EMAIL` | 开发者邮箱 | ❌ |

## 高级配置

### 使用内存中的 GPG 密钥

如果需要使用内存中的 GPG 密钥，可以在 `maven-publish.gradle` 中修改签名配置：

```gradle
signing {
    def gpgKeyId = project.findProperty("GPG_KEY_ID")
    def gpgPassword = project.findProperty("GPG_PASSWORD")
    def gpgSecretKeyRingFile = project.findProperty("GPG_SECRET_KEY_RING_FILE")
    if (gpgKeyId && gpgPassword && gpgSecretKeyRingFile) {
        useInMemoryPgpKeys(gpgKeyId, gpgPassword, file(gpgSecretKeyRingFile))
        sign publishing.publications.release
    }
}
```

然后在 `local.properties` 中配置：

```properties
GPG_KEY_ID=your_key_id
GPG_PASSWORD=your_key_password
GPG_SECRET_KEY_RING_FILE=/path/to/secring.gpg
```

## 故障排除

### GPG 签名失败
- 确保 GPG 已正确安装
- 确保密钥已导入：`gpg --list-keys`
- 确保密钥已上传到密钥服务器

### 认证失败
- 检查 `local.properties` 中的用户名和密码是否正确
- 确保 Maven Central 账号有发布权限

### 发布后找不到
- 等待同步（通常需要几小时）
- 检查 Sonatype Nexus 中的 staging repository 状态

## 示例项目

可以参考当前项目的 `shinebuttonlib/build.gradle` 作为完整示例。
