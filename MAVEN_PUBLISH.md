# Maven Central 发布指南

## 配置说明

项目已配置好 Maven Central 发布功能，使用以下信息：

- **Group ID**: `com.github.xk`
- **Artifact ID**: `shinebutton`
- **Version**: `2.6`
- **Maven Central 仓库**: `https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/`

## 发布步骤

### 1. 配置 GPG 签名（必需）

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

### 2. 发布到 Maven Central

#### 方式1: 使用 Gradle 命令发布
```bash
# 发布到 Maven Central
./gradlew :shinebuttonlib:publishReleasePublicationToMavenCentralRepository

# 或者发布所有变体
./gradlew :shinebuttonlib:publish
```

#### 方式2: 使用 Android Studio
1. 打开 Gradle 面板
2. 找到 `shinebuttonlib` -> `publishing` -> `publishReleasePublicationToMavenCentralRepository`
3. 双击执行

### 3. 在 Sonatype Nexus 中发布

发布后需要到 Sonatype Nexus 进行以下操作：

1. 访问: https://s01.oss.sonatype.org/
2. 使用 Maven Central 账号登录（用户名: FrA0wg）
3. 进入 "Staging Repositories"
4. 找到你的 staging repository
5. 点击 "Close" 按钮（验证通过后）
6. 点击 "Release" 按钮发布到 Maven Central

### 4. 等待同步

发布后需要等待同步到 Maven Central（通常需要几小时到一天）。

## 使用方式

发布成功后，可以在项目中这样使用：

### Gradle (Kotlin DSL)
```kotlin
dependencies {
    implementation("com.github.xk:shinebutton:2.6")
}
```

### Gradle (Groovy)
```groovy
dependencies {
    implementation 'com.github.xk:shinebutton:2.6'
}
```

### Maven
```xml
<dependency>
    <groupId>com.github.xk</groupId>
    <artifactId>shinebutton</artifactId>
    <version>2.6</version>
</dependency>
```

## 注意事项

1. **GPG 签名是必需的**：确保已正确配置 GPG 密钥
2. **版本号**：每次发布需要更新版本号
3. **认证信息**：Maven Central 的用户名和密码已配置在 `local.properties` 中
4. **首次发布**：首次发布到 Maven Central 需要先在 Sonatype 创建 issue 申请权限

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
