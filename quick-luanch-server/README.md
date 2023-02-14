# quick-launch-demo

## 基于SpringBoot的快速启动方案

``` Java
"项目目录结构"
|-- quick-launch-demo 
|    |-- pom.xml
|    |-- README.md
|    |-- quick-launch-auth       认证相关
|    |-- quick-launch-common     通用相关
|    |-- quick-launch-domain     持久层相关
|    |-- quick-launch-file       文件相关
|    |-- quick-launch-platform   后台相关
|    |-- quick-launch-start      启动相关
|    |   |-- pom.xml
|    |   |-- src-- main
|    |       |-- java
|    |       |   |-- com-- quick-- start
|    |       |       |-- QuickLaunchStartApplication.java 启动类
|    |       |-- resources
|    |       |   |-- config
|    |       |       |-- application.yml 项目配置yml
```

#### 一些想说的话

- 造个轮子