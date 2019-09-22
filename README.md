# user-management
A 'Hello World' type CRUD user management demonstration.

## how to run

```
git clone https://github.com/gaocuiqun/user-management.git
cd user-management
mvn package
java -jar user-management-app/target/user-management-1.0-SNAPSHOT.war
```

···
mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY '123456' PASSWORD EXPIRE NEVE
R;
Query OK, 0 rows affected (0.14 sec)

mysql> ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '1
23456';
Query OK, 0 rows affected (0.09 sec)

mysql> FLUSH PRIVILEGES;
···
