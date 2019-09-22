# git介绍：

- https://blog.csdn.net/u010839779/article/details/81177429
- https://www.cnblogs.com/tugenhua0707/p/4050072.html

## 重点五个命令：

1. 从Github克隆仓库到本地，例子：

```
git clone https://github.com/gaocuiqun/hello.git
```

2. 打包项目

```
mvn package
```

3. 执行打包好的程序

```
java -jar hello-app/target/hello-1.0-SNAPSHOT.war
```

4. 添加文件或文件夹到当前提交

```
git add hello
```
如果添加所有的更新

```
git add -A
```

5. 提交

```
git commit –m “注释”
```

6. 将提交推送到Github

```
git push
```

7. 从Github拉取更新

```
git pull
```

