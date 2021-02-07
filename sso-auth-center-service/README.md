# sso-auth-center

统一认证中心服务端


工程模块介绍

| **工程模块** | **名称** | **备注** |
| --- | --- | --- |
| sso-auth-center-service | 父工程 | 定义统一依赖版本等 |
| sso-auth-center-admin | 后台web，统一开放网关 | boot项目，后台controller层 |
| sso-business | dao与业务层 | dao层，service层 |
| sso-common  | 公共部分 | 工具类、常量类、注解、枚举、业务自定义异常 |
| sso-framework | 核心配置 | 数据源配置、redis配置、验证码、统一异常处理 等 |



类命名规范<br />

| **所属层级** | **名称规则** | **备注** |
| --- | --- | --- |
| DAO层 | entity | entity包下与数据库表一致的实体 |
|  | xxxMapper | 与表一致的Mapper |
|  | xxxDTO | 中间转换对象，比如关联sql的返回对象 |
|  | xxxQuery | dao层查询对象 |
| service层 | xxxBaseService | 公共service，比如用户公共接口，在业务service多次使用的方法 |
|  | xxxBaseServiceImpl |  |
|  | xxxService | 业务 service层 |
|  | xxxServiceImpl | 业务service层实现 |
| controller层入参 | xxxAddBO或xxxUpdateBO | 新增业务入参 |
|  | xxxEditBO | 修改业务入参 |
|  | xxxListPageBO | 分页查询入参需要继承公共分类 BasePageModel |
| controller层出参 | xxxPageVO或xxxListVO | 分页列表返回对象 |
|  | xxxDetailVO | 查询详情返回对象 |
|  | xxxCountVO | 统计业务返回对象 |
|  | xxOptionVO | 下拉选项返回对象 |




接口命名规范<br />

|  | **请求方式** | **备注** |
| --- | --- | --- |
| /listPage | get |  分页列表 |
| /listxxPage | get |  分页列表 |
| /listxxx | get | 不分页的列表 |
| /detail/{id} | get | _获取用户详细信息_ |
| /getxxx | get | 其它详情接口 |
| /add | post | 新增接口 |
| /update 或/edit | put或post | 修改接口 |
| /delete/{id} | / | 删除接口 |
|  |  |  |

公共返回类<br />

|  | **类名** | **常用方法** |
| --- | --- | --- |
| 公共类 | ResultModel | ResultModel._success_(业务object内容)<br />ResultModel._error_("失败原因") |
| 分页接口公共类 | ResultPageModel | ResultPageModel._success_(resultList,rowCount) |


分页接口统一返回结构<br />

| **参数** | **说明** |  |
| --- | --- | --- |
| code | 成功 | 200成功，非200失败 |
| total | 总条数 |  |
| data [] | 当前页内容列表 |  |
| msg |  |  |

JSON示例<br />

```json
{
    "code": 200,
    "total": 16213,
    "data": [
        {
            "id": 1,
            "name": "张三"
        }
    ],
    "msg": "操作成功"
}
```

<br />详情接口统一返回结构

| **参数** | **说明** |  |
| --- | --- | --- |
| code | 成功 | 200成功，非200失败 |
| data | object类型对象 |  |
| msg |  |  |

JSON示例<br />

```json
{
    "code": 200,
    "data": {},
    "msg": "操作成功"
}
```
