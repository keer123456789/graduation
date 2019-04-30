# graduation
## 介绍
本次毕设其中的一个方面，是针对BigChainDB进行SQL查询。一共有两个分支。master分支是java的桌面应用，将BigChainDB可视化，并使用SQL操作的工具。
之后导师觉得这个不实用，就让我改为网页班，的确桌面应用有很大问题，点击按钮触发事件响应不是很好，所以就在web分支做了一个网页版的可视化工具。
创建web分支之后，master分支没有更新，一直在更新web分支，所以可能master分支会有bug，之后会跟进更新master…………

## web分支使用

web分支是一个springboot项目，拉完源码后，启动项目，启动完成后，访问：[http://127.0.0.1:8080/index.html](http://127.0.0.1:8080/index.html)

# 注意
根目录下有 keypair.txt文件，这是BigChainDB中使用的密钥，我是写死的，网页中直接点击 获取密钥 按钮即可。 生成密钥的方法在/src/main/java/com/keer/graduation/Bigchaindb/KeyPairHolder.java
中，SaveKeyPairToTXT此方法是生成密钥到txt。
