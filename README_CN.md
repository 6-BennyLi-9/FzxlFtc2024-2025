## 通知

此仓库包含用于 INTO THE DEEP（2024-2025）赛季的公开 FTC SDK。

## 欢迎！
此 GitHub 仓库包含用于构建控制 *FIRST* Tech Challenge 竞赛机器人的 Android 应用程序的源代码。要使用此 SDK，请将整个项目下载/克隆到您的本地计算机。

## 要求
要使用此 Android Studio 项目，您需要 Android Studio Ladybug（2024.2）或更高版本。

如果您使用 Blocks 或 OnBot Java 编程您的机器人，则不需要 Android Studio。

## 入门
如果您是机器人技术的新手或 *FIRST* Tech Challenge 的新手，那么您应该考虑查看 [FTC Blocks 教程](https://ftc-docs.firstinspires.org/programming_resources/blocks/Blocks-Tutorial.html)，以熟悉如何使用控制系统：

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[FTC Blocks 在线教程](https://ftc-docs.firstinspires.org/programming_resources/blocks/Blocks-Tutorial.html)

即使您是高级 Java 程序员，从 [FTC Blocks 教程](https://ftc-docs.firstinspires.org/programming_resources/blocks/Blocks-Tutorial.html) 开始也是有帮助的，然后迁移到 [OnBot Java 工具](https://ftc-docs.firstinspires.org/programming_resources/onbot_java/OnBot-Java-Tutorial.html) 或 [Android Studio](https://ftc-docs.firstinspires.org/programming_resources/android_studio_java/Android-Studio-Tutorial.html)。

## 下载项目
如果您是 Android Studio 程序员，有几种方法可以下载此仓库。请注意，如果您使用 Blocks 或 OnBot Java 工具编程您的机器人，则不需要下载此仓库。

* 如果您是 git 用户，您可以克隆仓库的最新版本：

<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;git clone https://github.com/FIRST-Tech-Challenge/FtcRobotController.git</p>

* 或者，如果您愿意，您可以使用主仓库页面上的“下载 Zip”按钮。将项目下载为 .ZIP 文件将使下载大小更易于管理。

* 您还可以从仓库的 [Releases](https://github.com/FIRST-Tech-Challenge/FtcRobotController/releases) 页面的下载部分下载项目文件夹（作为 .zip 或 .tar.gz 存档文件）。

* Releases 页面还包含预构建的 APK。

下载并解压缩（如果需要）文件夹后，您可以使用 Android Studio 导入文件夹（“导入项目（Eclipse ADT, Gradle 等）”）。

## 获取帮助
### 用户文档和教程
*FIRST* 维护了在线文档，其中包含有关如何使用 *FIRST* Tech Challenge 软件和机器人控制系统的信息和教程。您可以使用以下链接访问此文档：

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[FIRST Tech Challenge 文档](https://ftc-docs.firstinspires.org/index.html)

请注意，在线文档是一个“常青”文档，会不断更新和编辑。它包含有关 *FIRST* Tech Challenge 软件和控制系统的最新信息。

### Javadoc 参考材料
FTC SDK 的 Javadoc 参考文档现已在线提供。点击以下链接以查看 FTC SDK Javadoc 文档作为实时网站：

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[FTC Javadoc 文档](https://javadoc.io/doc/org.firstinspires.ftc)

### 在线用户论坛
有关控制系统或 FTC SDK 的技术问题，请访问 FIRST Tech Challenge 社区网站：

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[FIRST Tech Challenge 社区](https://ftc-community.firstinspires.org/)

### 示例 OpModes
此项目包含大量示例 OpModes（机器人代码示例），可以将其剪切并粘贴到您的 /teamcode 文件夹中以按原样使用，或根据您团队的需求进行修改。

示例文件夹：&nbsp;&nbsp; [/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples](FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples)

位于 [/TeamCode/src/main/java/org/firstinspires/ftc/teamcode](TeamCode/src/main/java/org/firstinspires/ftc/teamcode) 文件夹中的 readme.md 文件包含示例命名约定的解释以及如何将它们复制到您自己的项目空间的说明。

# 发布信息

## 版本 10.2 (20250121-174034)

### 增强功能
* 添加了上传 Limelight3A 管道的功能，使团队可以版本控制他们的 Limelight 管道。

### 错误修复
* 修复了一个内部错误，即在指定目标位置之前指定 RUN_TO_POSITION 运行模式时，恢复需要重新启动。此修复的一个副作用是，错误位置的堆栈跟踪始终会在日志中生成。修复了问题 [1345](https://github.com/FIRST-Tech-Challenge/FtcRobotController/issues/1345)。
* 如果在构建 PredominantColorProcessor 时将感兴趣区域设置为 null，则会抛出有用的异常。还将默认 RoI 设置为全帧。解决了问题 [1076](FIRST-Tech-Challenge/FtcRobotController#1076)。
* 如果用户尝试使用格式错误的边界构造 ImageRegion，则会抛出有用的异常。解决了问题 [1078](FIRST-Tech-Challenge/FtcRobotController#1078)。

## 版本 10.1.1 (20241102-092223)

### 重大变更
* 支持 Android Studio Ladybug。需要 Android Studio Ladybug。

### 已知问题
* Android Studio Ladybug 的捆绑 JDK 是版本 21。JDK 21 已弃用对 Java 1.8 的支持，Ladybug 会警告此弃用。
  OnBotJava 仅支持 Java 1.8，因此，为了确保使用 Android Studio 开发的软件可以在 OnBotJava 环境中运行，SDK 的 targetCompatibility 和 sourceCompatibility 版本仍为 VERSION_1_8。
  FIRST 决定，在将 OnBotJava 迁移到更新版本的 Java 之前，弃用是两个非理想情况中较小的一个。

### 增强功能
* 为 Pose2D 添加了 `toString()` 方法。
* 为 SparkFunOTOS.Pose2D 添加了 `toString()` 方法。

## 版本 10.1 (20240919-122750)

### 增强功能
* 添加了新的基于 OpenCV 的 `VisionProcessor`s（可以附加到 Java 或 Blocks 中的 VisionPortal），以帮助团队在 INTO THE DEEP 游戏中通过计算机视觉实现颜色处理。
  * `ColorBlobLocatorProcessor` 实现了 OpenCV 颜色“斑点”检测。新的示例程序 `ConceptVisionColorLocator` 演示了其用法。
    * 提供了在预定义颜色范围或创建自定义 RGB、HSV 或 YCrCb 颜色空间之间的选择。
    * 提供了将检测限制在屏幕上指定感兴趣区域的功能。
    * 提供了应用侵蚀/膨胀形态学处理到阈值掩码的功能。
    * 提供了对返回数据进行排序和过滤的功能。
  * `PredominantColorProcessor` 允许使用相机的一个区域作为“长距离颜色传感器”来确定该区域的主要颜色。新的示例程序 `ConceptVisionColorSensor` 演示了其用法。
    * 确定的主要颜色是从一组离散的颜色“样本”中选择的，类似于 MINDSTORMS NXT 颜色传感器。
  * 有关此颜色处理功能的文档可以在此处找到：https://ftc-docs.firstinspires.org/color-processing
* 添加了颜色传感器的 Blocks 示例程序：RobotAutoDriveToLine 和 SensorColor。
* 更新了自检功能，将不匹配的 RC/DS 软件版本标识为“警告”而不是“失败”。

### 错误修复
* 修复了 [AngularVelocity 转换回归](https://github.com/FIRST-Tech-Challenge/FtcRobotController/issues/1070)。

## 版本 10.0 (20240828-111152)

### 重大变更
* 删除了用于 TensorFlow 对象检测的 Java 类和 Blocks。
* `AngularVelocity.unit`（类型为 `AngleUnit`）已重命名为 `AngularVelocity.angleUnit`（类型为 `UnnormalizedAngleUnit`）。

### 增强功能
* 添加了 REV 数字指示器的示例 - ConceptRevLED。
* 添加了对 [Sparkfun QWIIC LED Stick](https://www.sparkfun.com/products/18354) 的支持。
  * 要直接连接它，您需要此 [电缆](https://www.sparkfun.com/products/25596)。
* 添加了 ConceptLEDStick OpMode。
* 添加了黑色、蓝色、青色、深灰色、灰色、绿色、浅灰色、洋红色、红色、白色和黄色的 Blocks。
* 添加了一个“评估但忽略结果”的 Block，它执行连接的 Block 并忽略结果。允许您调用函数并忽略返回值。
* 添加了 Maxbotix Maxsonar I2CXL 声纳测距仪的 I2C 驱动程序。
* 添加了用于伺服和 CR 伺服的 setPwmEnable、setPwmDisable 和 isPwmEnabled 的 Blocks。
* 在 Blocks 编辑器中：ExportToBlocks 注释的注释字段中的 \n 显示为换行符。
* Telemetry 新增了 setNumDecimalPlaces 方法。
* Telemetry 现在格式化双精度和浮点数（不在对象内部，仅单独）。
* 添加了对 Limelight 3A 的支持。
* 添加了对 REV Servo Hub 的初步支持。
  * 机器人控制器和驱动程序站都需要更新到 10.0 版本，才能将 Servo Hub 配置为 Servo Hub。如果任一设备上的应用程序过时，Servo Hub 将显示为扩展 Hub，并且某些功能将无法按预期工作。在驱动程序站和机器人控制器应用程序都更新到 10.0 版本之前，您应该等待创建包含 Servo Hub 的配置。
  * 目前只能使用 REV 硬件客户端更新 Servo Hub 的固件并更改其地址。
* 添加了对 REV 9 轴 IMU (REV-31-3332) 的支持。
  * REV 9 轴 IMU 仅支持 [通用 IMU 接口](https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html)。
  * 添加了 `Rev9AxisImuOrientationOnRobot` Java 类。
  * 如果您在心理上将此 IMU 的 I2C 端口替换为控制 Hub 的 USB 端口，`RevHubOrientationOnRobot` 也与此传感器兼容。
  * 添加了 Rev9AxisImuOrientationOnRobot 的 Blocks，包括 RevHubImuOrientationOnRobot.xyzOrientation 和 RevHubImuOrientationOnRobot.zyxOrientation。
  * 添加了 Blocks 示例 SensorRev9AxisIMUOrthogonal 和 SensorRev9AxisIMUNonOrthogonal。
* 改进了 RevHubImuOrientationOnRobot 的 Blocks 支持。
  * 添加了 RevHubImuOrientationOnRobot.xyzOrientation 和 RevHubImuOrientationOnRobot.zyxOrientation 的 Blocks。
  * 添加了 Blocks 示例 SensorHubIMUOrthogonal（替换 SensorIMU）和 SensorHubIMUNonOrthogonal。
* 更新了 EasyOpenCV、AprilTag、OpenCV 和 `libjpeg-turbo` 版本。
* 添加了接受两个数字的 max 和 min 的 Blocks。
* 添加了 Blocks OpModes ConceptRevSPARKMini、RobotAutoDriveByEncoder、RobotAutoDriveByGyro、RobotAutoDriveByTime、RobotAutoDriveToAprilTagOmni 和 RobotAutoDriveToAprilTagTank。
* 两个同名的 OpModes 现在会自动重命名为名称后跟“-”和类名，允许它们同时存在于设备上。
* 在机器人控制器控制台的管理页面上显示活动配置的名称。
* 更新了 INTO THE DEEP 的 AprilTag 库。值得注意的是，`getCurrentGameTagLibrary()` 现在返回 INTO THE DEEP 标签。
* 添加了 Telemetry.setMsTransmissionInterval 和 Telemetry.getMsTransmissionInterval 的 Blocks。
* 添加了 Blocks 示例 SensorOctoQuad。

### 错误修复
* 修复了 RevBlinkinLedDriver Blocks 在 Blocks 编辑器工具箱中位于执行器下的错误。它们现在位于其他设备下。
* 修复了在驱动程序站请求停止后，用户代码中抛出的 `Exception` 会被静默吞掉的错误。
* 修复了如果您请求 `AngularVelocity` 的单位与设备报告的单位不同，它会将其归一化到 -PI 和 PI 之间（弧度）或 -180 和 180 之间（度）的错误。
