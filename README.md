# dasinongc

20150527 网络层等基础代码提交，慢慢完善中...    --liam

# 此处暂时用来记录restful api 相关文档
## 开发中借口

参数后的(R)表示此字段必填。有疑问字段和返回码用（?）标注
底层错误一律返回码500加描述。

| 接口   | 接口路径  | 输入参数 |　返回码 | 返回描述    |　　返回内容  |
|--------|-----------|----------|---------|-------------|------------------|
|  首页    |  home    |   fieldId     | 200 | 获取消息成功            |Field(包括相关petDisSpec,natDisSpec,Task等)，Weather|
|          |          |               | 110 | 用户没有田地            |                             |
|          |          |               | 100 | 用户未登陆         |                             |
|          |          |               | 120 | 用户id不存在         |                             |
|          |  updateTask |  fieldId(R) | 200 |  任务状态更新成功 |                        |      
|          |        |  taskId,taskStatus|| taskIds,taskStatus | 200 | 任务状态列表跟新成功  |                        |    
|          |   |  taskIds,taskStatuss     | 300 |  |                        |


## 待开发接口
| 接口   | 接口路径  | 输入参数 |　返回码 | 返回描述    |　　返回内容  |
|--------|-----------|----------|---------|-------------|------------------|
|  通用    |  checkUser    |  cellPhone(R) |  200   | 用户存在　　|          |
| 　　　   |  　　　　　   |  　　　　　　|  100   | 用户不存在　　|          |



## 完成借口
| 接口   | 接口路径  | 输入参数 |　返回码 | 返回描述    |　　返回内容  |
|--------|-----------|----------|---------|-------------|------------------|
| 注册   | regUser | userName     | 200  |  成功         |    User(包括fieldList)       |
|        |         | password(R)  | 500  | 密码不能为空   |            |
|        |         | cellphone(R) | 500  |电话号码不能为空　|         | 
|        |         | address      |   |   |          |
|--------|---------|-------------|-------|-----------|------------------|
| 登陆   |  login  |  userName(R)   |  200  |  成功     |　　User(包括fieldList)　　　　|
|        |         |  password(R)  |   200  | 已经登陆　|    User(包括fieldList)        |
|        |         |               |   110  | 用户不存在|                  |
|        |         |               |   120  | 密码错误  |                  |
| 认证注册 |  authRegLog | cellphone(R)  | 200 | 注册成功，初始密码手机后６位　|    User(包括fieldList)      |
|          |          |               | 200 | 用户已存在，登陆　　　　　　　|    User(包括fieldList)      |
|          |          |               | 200 | 用户已登陆　　　　　　　|    User(包括fieldList)      |
