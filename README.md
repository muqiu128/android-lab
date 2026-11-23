# android-lab

手机 / 手表 Android 小工具的开发区。构建全部在 GitHub Actions 云端完成，不依赖任何一台固定电脑。

## 手机上怎么装

**方式一（最稳，国内网络首选）**：电脑上下载 APK，用微信"文件传输助手"发到手机，点开安装。

**方式二（手机直接下）**：发布页里点 `app-debug.apk`
<https://github.com/muqiu128/android-lab/releases>

> 手机直连 GitHub 可能慢或失败，失败就走方式一，别在网络上耗时间。

## 怎么发新版本

```bat
call D:\AndroidDev\env.cmd
cd /d D:\AndroidDev\repos\android-lab
git tag -a v0.2 -m "这次改了什么"
git push origin v0.2
```

推完 3–6 分钟，Release 里就有新的 APK 了。日常小改不用打标签，
直接 push 到 main 也能构建，只是 APK 在 Actions 的 artifact 里，手机上不好拿。

## 构建失败了怎么办

手机上只会看到"没有新包"，不会告诉你哪里错。去看日志：

1. 打开 <https://github.com/muqiu128/android-lab/actions>
2. 点红色的那次运行 → 点 `Build debug APK` 那一步
3. 报错一般在最后 30 行里，把那段发我，或者直接让我查

本地能编译通过、云端却失败，八成是：
- 引了只在本地存在的文件（`local.properties` 这类，已被 .gitignore 排除，不要提交）
- 依赖用了国内镜像才有的东西（`settings.gradle` 里 google() 排在阿里云前面，两边都能解析）


## 本地怎么改（教室电脑上）

```bat
call D:\AndroidDev\env.cmd
cd /d D:\AndroidDev\repos\android-lab
gradle assembleDebug
```

产物：`app\build\outputs\apk\debug\app-debug.apk`

本地有设备时可直接推到设备：

```bat
adb install -r app\build\outputs\apk\debug\app-debug.apk
adb logcat -s BuddyLab:D AndroidRuntime:E
```

## 提交代码

双击 `D:\AndroidDev\repos\android-lab\push.cmd`，或手动：

```bat
git add -A
git commit -m "说明这次改了什么"
git push
```

push 完等 2–3 分钟，手机重新下载安装即可。

## 目录

- `app/` 应用主体（Java，minSdk 30 / targetSdk 34）
- `PROGRESS.md` **项目状态快照，每次有进展就更新，换机器/重开会话先读它**
- `.github/workflows/build.yml` 云端构建配置

## 加新工具的思路

一个 App 里放多个小工具页比开一堆仓库好维护：在 `MainActivity` 里加入口，每个工具一个独立类。
