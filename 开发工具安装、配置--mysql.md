# mysql

## 直接解压mysql-advanced-5.7.27-win32.zip

1. 解压后，初始化MySQL：

```
mysqld --initialize
```

   执行完成后，会输出 root 用户的初始默认密码

2. 安装成为Windows服务：

```
mysqld install
```

3. 启动 MySQL：

```
start mysqld
```

4. Mysql安装成功后，默认的root用户密码为空，需要创建root用户的密码：

```
mysqladmin -u root password "new_password";
```

5. 连接到Mysql服务器：

```
mysql -u root -p
Enter password:*******     
```

6. 解压mysql-gui-tools-noinstall-5.0-r17-win32，用MySQLQueryBrowser工具创建数据库、创建表

## 运行安装程序mysql-installer-commercial-8.0.17.0.msi：

    https://blog.csdn.net/bobo553443/article/details/81383194
    注意安装的时候要输入用户名、密码
    安装完成后可以用workbench来创建数据库、创建表

## 遇到的问题

1. 命令行可访问mysql，但gui工具连接不上mysql

```
mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
```

2. 修改密码：

```
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '123456' PASSWORD EXPIRE NEVER;
```

3. 忘记密码无法登陆：

停止MySQL服务，删除data文件夹，重新运行mysqld --initialize

