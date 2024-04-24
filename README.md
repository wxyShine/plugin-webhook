# plugin-webhook

## 🎟️简介

`plugin-webhook` 是一个为 [Halo](https://halo.run/) （强大易用的开源建站工具）设计的Webhook插件。
Webhook 是一种通过 HTTP 协议实现的回调机制，允许用户在特定事件发生时（如文章发布、新评论、回复评论等事件）将数据推送到指定的 Webhook URL。这种机制使得用户可以实时地将事件通知发送到外部系统，从而实现与其他服务的集成。

![img01](https://cdn.wxy97.com/blog/2024-04-24-zrrkqrzy.png)
> 点击测试时 如果你配置的Webhook URL 无误将会收到一条如下数据:
```json
{
  "eventType": "TEST_WEBHOOK",
  "eventTypeName": "测试webhook",
  "hookTime": "2024-04-24 16:16:51",
  "data": "Test webhook success"
}
```

> 更多事件详细说明请参考使用文档: https://www.wxy97.com/archives/2135a7d6-e40a-4bae-b4a6-06db588b97aa

## ✨功能

- **事件触发**：当在Halo中发生特定事件（如文章发布、新评论、回复评论等事件）时，触发Webhook调用。
- **灵活配置**：用户可以配置Webhook URL
- **简单集成**：易于在现有的Halo博客系统中集成和使用。

## 💻安装
如何安装和配置`plugin-webhook`插件。


1. 下载，目前提供以下下载方式：
   GitHub Releases：访问 [Releases](https://github.com/wxyShine/plugin-webhook/releases) 下载 Assets 中的 JAR 文件。  
   安装，插件安装和更新方式可参考：https://docs.halo.run/user-guide/plugins  
   安装完成之后，记得启用插件并完成相关配置。


## 📒[TODO](https://github.com/wxyShine/plugin-webhook)
- [x] TEST_WEBHOOK("测试webhook")
- [x]  NEW_POST("发布文章")
- [x]  DELETE_POST("删除文章")
- [x]  NEW_COMMENT("发表评论")
- [x]  DELETE_COMMENT("删除评论")
- [x]  REPLY_COMMENT("回复评论")
- [x]  DELETE_REPLY_COMMENT("删除回复")
- [ ]  发布瞬间
- [ ]  删除瞬间
- [ ]  可配置事件
  ...


## ✍️贡献
如果您有兴趣为plugin-webhook贡献代码或文档，欢迎Fork本仓库并提交[Pull Request](https://github.com/wxyShine/plugin-webhook/pulls)。


## ⛓️支持
如果您在使用halo-plugin-webhook时遇到任何问题，可以通过以下方式寻求帮助：

提交[GitHub Issue](https://github.com/wxyShine/plugin-webhook/issues)。
