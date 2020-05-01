## 图库爬虫

### 作用
根据几个网络上找到的在线随机图片接口, 爬下来存到本地,以供自己使用
可以作为博客的随机图片背景

### 为什么不直接使用
1. 图片地址是国外的, 在线加载较慢, 使用体验不是太好
2. 想自己对图片做一些筛选


### 如何使用??
1. 本地使用

下载代码,然后配置好 `save.path` 为本地实际路径,运行项目即可


2. Docker 部署

```bash
    docker run -d --restart always --name joy-gallery-spider -p 8080:8080 -e logLevel=info -v /path:/home/liufa/app/joy-gallery-spider/gallery joyfay/joy-gallery-spider
```

若要配置 application.yml 里的变量, 请通过环境变量 `args` 指定, 例如:

```bash
    -e "args= --logging.level.org.springframework.web.client.RestTemplate=info --logging.level.cn.joylau.code=info"
```