# 简化版发布指南（无需 GPG 签名）

## 方案选择

### 方案1: GitHub Packages（推荐，最简单）⭐

**优点：**
- 无需 GPG 签名
- 配置简单
- 与 GitHub 仓库集成
- 免费使用

**使用方法：**

1. 在 `shinebuttonlib/build.gradle` 中，将：
   ```gradle
   apply from: rootProject.file('maven-publish.gradle')
   ```
   改为：
   ```gradle
   apply from: rootProject.file('maven-publish-simple.gradle')
   ```

2. 创建 GitHub Personal Access Token：
   - 访问：https://github.com/settings/tokens
   - 点击 "Generate new token (classic)"
   - 勾选 `write:packages` 和 `read:packages` 权限
   - 复制生成的 token

3. 在 `local.properties` 中添加：
   ```properties
   GITHUB_USERNAME=XiongKe94
   GITHUB_TOKEN=your_github_token_here
   ```

4. 发布：
   ```bash
   ./gradlew :shinebuttonlib:publishReleasePublicationToGitHubPackagesRepository
   ```

5. 使用方式：
   ```gradle
   repositories {
       maven {
           url = uri("https://maven.pkg.github.com/XiongKe94/ShineButton")
           credentials {
               username = "XiongKe94"
               password = "your_github_token"
           }
       }
   }
   
   dependencies {
       implementation 'com.github.xk:shinebutton:2.6'
   }
   ```

---

### 方案2: JitPack（最简单）⭐

**优点：**
- 无需任何配置
- 只需 GitHub 仓库
- 自动构建和发布
- 完全免费

**使用方法：**

1. 确保代码已推送到 GitHub
2. 创建 Release 或 Tag：
   ```bash
   git tag -a v2.6 -m "Release version 2.6"
   git push origin v2.6
   ```

3. 访问 https://jitpack.io/
4. 输入你的 GitHub 仓库地址：`XiongKe94/ShineButton`
5. 点击 "Look up"，JitPack 会自动构建
6. 使用方式：
   ```gradle
   repositories {
       maven { url 'https://jitpack.io' }
   }
   
   dependencies {
       implementation 'com.github.XiongKe94:ShineButton:2.6'
   }
   ```

**注意：** Group ID 格式为 `com.github.你的GitHub用户名`

---

### 方案3: 私有 Maven 仓库

如果你有自己的 Maven 仓库服务器，可以配置：

1. 在 `maven-publish-simple.gradle` 中取消注释私有仓库配置
2. 在 `local.properties` 中添加认证信息
3. 发布到你的仓库

---

## 推荐方案对比

| 方案 | 难度 | 需要配置 | 需要签名 | 推荐度 |
|------|------|---------|---------|--------|
| GitHub Packages | ⭐⭐ | Token | ❌ | ⭐⭐⭐⭐⭐ |
| JitPack | ⭐ | 无 | ❌ | ⭐⭐⭐⭐⭐ |
| Maven Central | ⭐⭐⭐⭐⭐ | 很多 | ✅ | ⭐⭐⭐ |

## 快速开始（推荐 JitPack）

1. 提交代码到 GitHub
2. 创建 Tag：
   ```bash
   git tag v2.6
   git push origin v2.6
   ```
3. 访问 https://jitpack.io/，输入仓库地址
4. 完成！可以直接使用了

## 快速开始（推荐 GitHub Packages）

1. 修改 `shinebuttonlib/build.gradle` 使用 `maven-publish-simple.gradle`
2. 配置 GitHub Token
3. 运行发布命令
4. 完成！
