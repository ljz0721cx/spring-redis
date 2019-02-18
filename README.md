# spring-redis
模拟redis在实际场景中的使用




# Mac安装客户端
## 安装brew cask : 在终端中输入下面语句

```shell
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)" < /dev/null 2> /dev/null ; brew install caskroom/cask/brew-cask 2> /dev/null
```
输入对应的密码，等待cask安装完成

## 安装Redis Desktop Manager
安装完cask之后，在终端中输入以下命令
```
brew cask install rdm
```     

## 直接安装 rdm
已经下载好的文件 
spring-redis/bule-redis/redis-desktop-manager-0.9.4.44.dmg


# 实际设计使用
生产消费代码
/Users/lijianzhen1/cmyworkspace/spring-redis/bule-redis/src/main/java/com/bule/redis/pubSub

库存抢占削流后代码
/Users/lijianzhen1/cmyworkspace/spring-redis/bule-redis/src/main/java/com/bule/redis/saleProm

分片使用的代码
/Users/lijianzhen1/cmyworkspace/spring-redis/bule-redis/src/main/java/com/bule/redis/sharding

分布式锁代码
/Users/lijianzhen1/cmyworkspace/spring-redis/bule-redis/src/main/java/com/bule/redis/setnx
