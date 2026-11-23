# PROGRESS

> 这个文件是项目的大脑。任何一台机器、任何一次新会话，先读这里就能接上。
> 有进展就更新，不要攒着。

## 环境（换机器时照这个重建）

- 工具链全在 D 盘：JDK 17 `D:\AndroidDev\jdk\jdk-17.0.2`，SDK `D:\AndroidDev\sdk`，Gradle 8.9 `D:\AndroidDev\gradle\gradle-8.9`
- 环境变量：`call D:\AndroidDev\env.cmd`
- 本机 `sdk.dir` 写在 `local.properties`（不进仓库）
- 托管：GitHub `muqiu128/android-lab`，SSH 密钥 `D:\AndroidDev\ssh\id_ed25519`（不进仓库）
- 构建：GitHub Actions，push 自动出 APK

## 当前状态

- [x] 仓库骨架 + Actions 工作流
- [x] 基础 App：设备信息页 + 应用内日志 + 崩溃落盘
- [x] 云端构建闭环：push → build → tag → Release 出 APK（v0.1 已发布）
- [ ] 装到一加手表二上实测（Wear OS 小屏适配待验证）
- [ ] 无线 adb 到手表（需现场配对，重启失效）
- [ ] 第一个真实小工具（待定需求）

## 待办 / 想法

- 手表端界面适配（小屏、圆屏）
- 日志上报：目前靠复制文本，后续可加一键分享到微信

## 决策记录

- 2026-11-23 构建放 GitHub Actions 而非云服务器：零预算，且不受教室电脑关机影响
- 2026-11-23 选 Java 而非 Kotlin：少一层依赖下载，构建更快，够用
- 2026-11-23 minSdk 30：覆盖一加手表二（底层 Android 11）与主流手机

## 踩过的坑

- 教室电脑 C 盘有 PowerShadow 影子系统，重启全回滚，所以一切产物必须落在 D 盘或云端
- 新版 curl 才支持 `--retry-all-errors`，本机是旧版，大文件下载要用 `-C -` 循环续传
- Gradle 首次构建 3 分半，之后靠缓存会快很多
