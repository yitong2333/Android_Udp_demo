# Android UDP 通信 Demo

这是一个简单的 Android UDP 通信演示项目，用于展示如何在 Android 应用中使用 UDP 协议进行通信。

## 功能特点

- 发送和接收 UDP 数据包
- 简单的演示用例

## 安装

1. 克隆项目到本地：

   ```bash
   git clone https://github.comyitong2333/Android_Udp_demo.git
   ```

2. 使用 Android Studio 打开项目。

3. 在 Android Studio 中构建和运行应用。

## 使用示例

1. 启动应用后，你将看到一个简单的用户界面。
2. 输入目标 IP 地址和端口号。
3. 点击 "发送消息" 按钮，演示应用将发送 UDP 数据包到指定的目标。
4. 接收方将显示接收到的 UDP 数据包信息。

## 代码示例

以下是简化的发送 UDP 数据包的示例代码：

```java
// 在发送方
String message = "Hello, UDP!";
DatagramSocket socket = new DatagramSocket();
InetAddress targetAddress = InetAddress.getByName("目标IP地址");
int targetPort = 12345;

byte[] sendData = message.getBytes();
DatagramPacket packet = new DatagramPacket(sendData, sendData.length, targetAddress, targetPort);

socket.send(packet);
socket.close();
```

详细的代码示例和解释可以在项目源代码中找到。

## 注意事项

- 请确保你的设备和接收方设备在同一网络中，以确保 UDP 数据包能够正确传递。

## 贡献

欢迎贡献和提出建议！如果发现任何问题或有改进建议，请创建一个 Issue 或提交 Pull Request。

## 许可证

[MIT 许可证](LICENSE)
```
