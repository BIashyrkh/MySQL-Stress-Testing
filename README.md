# MySQL-Stress-Testing

## 一，10w 数据

### 1，插入数据
- 第一次 -- 15.504 s
- 第二次 -- 16.125 s 
- 第三次 --  16.524 s

### 2，全表查询 "SELECT COUNT(*) FROM  "+tableName+" WHERE realName = 'vector'";
- 第一次 -- 193 ms
- 第二次 -- 189 ms
- 第三次 -- 189 ms

### 3，查询100条数据 -- 返回全部数据 "SELECT * FROM "+tableName+" ORDER BY id DESC LIMIT "+startId+","+count+" ";
- 第1 条开始查询 -- 2 ms
- 第1w条开始查询 -- 14 ms
- 第40w条开始查询 -- 700 ms

### 4，查询100条数据 -- 返回Id 数据 "SELECT id FROM "+tableName+" ORDER BY id DESC LIMIT "+startId+","+count+" ";
- 第1 条开始查询 -- 1ms
- 第1w条开始查询 -- 2ms
- 第4w条开始查询 -- 75 ms

### 5，随机查询1w 条数据 -- 返回全部数据 "SELECT * FROM "+tableName+" where id in " + in;
- 第一次 -- 62ms
- 第二次 -- 49ms
- 第三次 -- 48ms

## 二，100w 数据
## 三，1000w 数据