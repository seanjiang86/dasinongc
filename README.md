# dasinongc


# 此处暂时用来记录restful api 相关文档
http://182.254.129.101/ploughHelper/

参数后的(R)表示此字段必填。有疑问字段和返回码用（?）标注
底层错误一律返回码500加描述。
用户未登陆(或session失效) 一律返回100
缺少参数/参数异常 3++

Issue to confirm:
Shall we use long for all date? currently input is yyyy/mm/dd, output is long.
测土相关api还未上传到server，宁宁调试前和希遥对一下状态。

Note,请先用authReg?cellphone=13999999191
登陆获得默认用户。
fieldId=10有相关任务

## 开发(等待测试)中借口
| 接口   | 接口路径  | 输入参数 |　返回码 | 返回描述    |　　返回内容  |
|--------|-----------|----------|---------|-------------|------------------|
|提交用户手机验证状态 |isAuth|  | 200  | ||
|    |  |  | 120  |尚未验证 ||
|个人信息编辑提交| updateProfile |userName,cellphone,password,address,pictureId,telephone  |  200   | 更新成功 |   | 
|修改个人信息（手机号、密码、姓名、地址、家庭电话）|updateProfile| 参数未出现部分不会被更新| 200 |更新成功||
|测土列表|loadReports|fieldId| 200| 检索成功| 默认用户全部测土报告或当前天地测土报告|
|测土提交|insertSoilReport| see sample. testDate can not be null.|200|更新成功||
| 短信订阅设置提交 | insertSubScribeList | see sample     | 200  |  成功         |           |
| 短信订阅清单获取      |   getSubScribeLists    |  | 200  | 成功  |   Map<订阅条目，条目编号〉     |
| 短信订阅获取 |  loadSubScribeList       | id  | 200  |成功　|  订阅内容    | 
| 短信订阅更改 |  updateSubScribeList    | id+insert sample   |  200 | 成功  |          |
| 短信订阅删除 |  deleteSubScribeList(还未上传）  | id  |  200 | 成功|   |  
| 获取老农段子|  getLaoNong  | id  |  200 | 成功|   |  
/insertSubScribeList?targetName=小张&cellphone=12112345678&province=北京&city=北京&country=朝阳区&district=某某街道&area=15.1&cropId=223&isAgriWeather=true&isNatAlter=false&isRiceHelper=false

insertSoilReport?userId=15&fieldId=10&type=type&color=red&fertility=fertility&humidity=30.5&testDate=2015/04/01&phValue=6.7&organic=organic&an=12.1&qn=10.2&p=100&qK=12&sK=1.2&fe=3.0&mn=12&cu=21.0&zn=1&b=90&mo=12.0&ca=1.0&s=0.1&si=45&mg=2.3

## 待开发接口
| 接口   | 接口路径  | 输入参数 |　返回码 | 返回描述    |　　返回内容  |
|--------|-----------|----------|---------|-------------|------------------|
|百科搜索|  |    keywords |200  |   | search result |
|app检测更新|  |     |  |   |  |
|首页返回 种地，打药，日历|  |     |  |   |  |
|编辑用户头像 ||||||
|获取用户短信订阅列表 ||||||
|首页天气 ||||||
|首页测土||||||
|测土修改||||||



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
|  通用    |  checkUser    |  cellphone(R) |  200   | 用户存在　　|          |
| 　　　   |  　　　　　   |  　　　　　　|  110   | 用户不存在　　|          |
|  首页    |  home    |   fieldId     | 200 | 获取消息成功            |Field(包括相关petDisSpec,natDisSpec,Task等)，Weather|
|          |          |               | 110 | 用户没有田地            |                             |
|          |          |               | 100 | 用户未登陆         |                             |
|          |          |               | 120 | 用户id不存在         |                             |
|          |  updateTask |  fieldId(R) | 200 |  任务状态更新成功 |                        |      
|          |        |  taskId,taskStatus|| taskIds,taskStatus | 200 | 任务状态列表跟新成功  |                        |    
|          |   |  taskIds,taskStatuss     | 300 |  |                        |
| 注册田地 |  createField   |  fieldName,isActive，        |  200       |   创建成功          |     field 相关信息      |
|          |                |  seedingortransplant,area,   |            |                     |                         |
|          |                |  startDate,locationId,  |             |               |                      |
|          |                |  varietyId,currentStageId(allR)  |          |               |                      |
| 选择品种 |  getVarietyList | cropName或cropId(推荐）  |  200   |  获取品种列表成功  | 小品种名列表，对应编号和品种id|      
|          |                 | locationId(推荐）或province   |   300    | 缺少参数 |     Map（Map（subId,varietyId））    |
| 选择地址 |  getLocation   | province(R),city(R),country(R),district(R) |  200 | 获取成功 |   Map(community,locationId) |
|         |                 |                                  | 300 | 缺少参数 ||
|搜索附近用户 | searchNearUser | lat(R),lon(R) |200 | 找到附近农户 | 农户数 |
|获得地址| searchLocation | lat(R),lon(R),province(R)，city（R）,country(R)    |200  | 找到最近田  | location内容 |
|获得所有任务| getAllTask | fieldId(R) | 200 | 获取任务成功 | 任务列表 |
|个人信息获取| loadUserProfile |     | 200  | 获取成功  | user内容 |
