1.  count(1) and count(*) and  count(列名)执行效率比较:
    1.  列名为主键，count(列名)会比count(1)快  
    2.  列名不为主键，count(1)会比count(列名)快  
    3.  如果表多个列并且没有主键，则 count（1） 的执行效率优于 count（*）  
    4.  如果有主键，则 select count（主键）的执行效率是最优的  
    5.  如果表只有一个字段，则 select count（*）最优。
2.  实例比较
    ```aidl
        mysql> create table counttest(name char(1), age char(2));
        Query OK, 0 rows affected (0.03 sec)
        
        mysql> insert into counttest values
        -> ('a', '14'),('a', '15'), ('a', '15'),
        -> ('b', NULL), ('b', '16'),
        -> ('c', '17'),
        -> ('d', null),
        ->('e', '');
        Query OK, 8 rows affected (0.01 sec)
        Records: 8 Duplicates: 0 Warnings: 0
        
        mysql> select * from counttest;
        +------+------+
        | name | age |
        +------+------+
        | a | 14 |
        | a | 15 |
        | a | 15 |
        | b | NULL |
        | b | 16 |
        | c | 17 |
        | d | NULL |
        | e | |
        +------+------+
        8 rows in set (0.00 sec)
        
        mysql> select name, count(name), count(1), count(*), count(age), count(distinct(age))
        -> from counttest
        -> group by name;
        +------+-------------+----------+----------+------------+----------------------+
        | name | count(name) | count(1) | count(*) | count(age) | count(distinct(age)) |
        +------+-------------+----------+----------+------------+----------------------+
        | a | 3 | 3 | 3 | 3 | 2 |
        | b | 2 | 2 | 2 | 1 | 1 |
        | c | 1 | 1 | 1 | 1 | 1 |
        | d | 1 | 1 | 1 | 0 | 0 |
        | e | 1 | 1 | 1 | 1 | 1 |
        +------+-------------+----------+----------+------------+----------------------+
        5 rows in set (0.00 sec)

    ```
    