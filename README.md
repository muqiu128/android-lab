# android-lab

手机 / 手表 Android 小工具的开发区。构建全部在 GitHub Actions 云端完成，不依赖任何一台固定电脑。

## 手机上怎么装

1. 打开 <https://github.com/muqiu128/android-lab/actions>，点最新一次成功的 `build`
2. 页面底部 Artifacts 里有 `apk-debug`，但手机下载 artifact 较麻烦，推荐下面这条：
3. 打 tag 时（`v1`、`v2`…）会生成 Release，APK 可直接下载：
   <https://github.com/muqiu128/android-lab/releases>

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
