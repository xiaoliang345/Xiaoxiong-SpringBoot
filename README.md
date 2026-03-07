## 项目简介

`xiaoxiong` 是一个基于 **Spring Boot** 的后端脚手架项目，集成了常用的基础能力，适合作为后端服务的起步模板进行二次开发。

### 技术栈
- **后端框架**: Spring Boot3.2.3
- **持久层**: MyBatis-Plus
- **数据库**: MySQL5.7/8.0
- **缓存**: Redis
- **认证与权限**: Sa-Token（基于 Token 的登录态、角色/权限控制）
- **对象存储**: 腾讯云 COS
- **接口文档**: Knife4j (基于 OpenAPI/Swagger)

## 本地开发与启动

### 环境准备
- 安装 JDK 17
- 安装并启动 MySQL，创建名为 `xiaoxiong` 的数据库
- 安装并启动 Redis
- 安装 Maven（如使用 IDEA 内置 Maven 可不单独安装）

### 配置说明

- 通用配置文件：`src/main/resources/application.yml`
  - 指定应用名、端口、MyBatis-Plus 配置、Knife4j 配置等。
- 本地开发配置：`src/main/resources/application-dev.yml`
  - **仅用于本地开发环境**，已被加入版本控制忽略列表，避免提交个人数据库账号和密码。
  - 你可以在其中配置开发环境使用的数据库和 Redis 连接信息，例如：

```yaml
db:
  username: root
  password: 123456

cos:
  client:
    secretId: 你的SecretId
    secretKey: 你的SecretKey
    region: ap-guangzhou        # 替换为你实际的存储桶所属地域
    bucket: your-bucket-name    # 替换为你实际的存储桶名称（形如 xxx-123456789）

langchain4j:
  open-ai:
    chat-model:
      base-url:  # 请替换为实际的base-url
      api-key:   # 请替换为实际的API密钥
      model-name:  # 请替换为实际的模型名称
      log-requests: true
      log-responses: true
      max-tokens: 8192
```

> 注意：请根据自己的本地环境修改用户名、密码、COS 凭证以及其它连接信息。
>
> 以上 `cos.client.*` 配置会通过 `application.yml` 中的占位符注入到 `tencent.cos.*`，由 `TencentCOSConfig` 创建 COS 客户端并供业务使用。

### 启动项目

1. 在项目根目录执行：

```bash
mvn spring-boot:run
```

或在 IDE 中运行启动类 `XiaoxiongApplication`。

2. 启动成功后服务默认访问地址：
- 基础接口路径：`http://localhost:8801/api`
- 接口文档（Knife4j）：可根据实际配置访问，一般为 `http://localhost:8801/api/doc.html` 或类似路径。

## 目录结构简要说明

### Java 源码目录：`src/main/java/com/oxn/xiaoxiong`

```text
com.oxn.xiaoxiong
├─ XiaoxiongApplication.java         // 应用启动类，入口 main 方法
├─ controller                        // 对外接口层（Controller）
│  ├─ MainController.java           // 根路径健康检查接口，返回 "ok"
│  ├─ UserController.java           // 用户相关接口，演示分页 + Redis 缓存 + Sa-Token 登录与角色校验
│  └─ FileController.java           // 文件上传 / 下载接口，基于腾讯云 COS
├─ service                           // 业务层（Service）
│  ├─ SysUserService.java           // 系统用户业务接口，继承 MyBatis-Plus IService
│  └─ impl
│     └─ SysUserServiceImpl.java    // 系统用户业务实现，继承 ServiceImpl
├─ mapper                            // 数据访问层（Mapper）
│  └─ SysUserMapper.java            // 用户表 Mapper，继承 BaseMapper<SysUser>
├─ domain                            // 领域实体
│  └─ SysUser.java                  // 系统用户实体，对应表 sys_user
├─ common                            // 通用模型与工具
│  ├─ BaseResponse.java             // 统一响应体封装
│  ├─ DeleteRequest.java            // 通用删除请求对象
│  ├─ PageRequest.java              // 通用分页请求参数
│  └─ ResultUtils.java              // 快速构造成功 / 失败响应工具
├─ enums                             // 枚举类
│  ├─ StatusCode.java               // 统一状态码枚举
│  └─ SysRoleEnum.java              // 系统用户角色枚举（user / admin / super-admin）
├─ exception                         // 统一异常处理
│  ├─ BusinessException.java        // 业务异常定义
│  ├─ GlobalExceptionHandler.java   // 全局异常捕获与返回封装，含 Sa-Token 登录/权限异常适配
│  └─ ThrowUtils.java               // 条件抛出业务异常的工具
├─ sa                               // Sa-Token 相关封装
│  ├─ StpKit.java                   // StpLogic 门面类，集中管理账号体系（SYS_ROLE）
│  ├─ StpInterfaceImpl.java         // 自定义权限加载实现，根据 SysUser + SysRoleEnum 返回角色/权限
│  ├─ annotation
│  │  └─ SaUserCheckRole.java       // 用户端角色校验注解（支持 AND/OR 多角色模式）
│  └─ aspect
│     └─ SaUserAuthAspect.java      // 用户端权限切面，基于 SaUserCheckRole 做登录 + 角色拦截
├─ config                            // 配置类
│  ├─ CorsConfig.java               // 全局 CORS 跨域配置
│  └─ TencentCOSConfig.java         // 腾讯云 COS 客户端与传输管理器配置
└─ util                              // 工具类
   ├─ RedisUtil.java                // Redis 操作工具（基于 Hutool JSON）
   └─ TencentCOSUtil.java           // 腾讯云 COS 上传 / 下载工具
```

### 其它目录和文件（概览）

- **`pom.xml`**：Maven 项目配置与依赖管理（Spring Boot、MyBatis-Plus、Redis、Knife4j 等）。
- **`src/main/resources`**：应用配置与 Mapper XML 文件，例如 `application.yml`、`application-dev.yml`、`mapper/SysUserMapper.xml`。
- **项目根目录脚本和文档**：`mvnw` / `mvnw.cmd`（Maven Wrapper）、`HELP.md`（Spring Boot 自动生成的帮助说明）。

## 注意事项

- 请勿将生产环境的敏感配置（数据库账号密码、第三方密钥等）写入仓库。
- 如需新增环境（如 `prod`、`test`），建议使用独立的配置文件并通过环境变量或配置中心管理敏感信息。

## 认证与权限（Sa-Token）说明

- **依赖引入**（已在 `pom.xml` 中配置）：
  - `sa-token-spring-boot3-starter`：核心登录、会话、权限控制能力
  - `sa-token-redis-template`：基于 Redis 持久化登录会话等数据
- **账号体系**：
  - 使用 `StpKit.SYS_ROLE` 代表系统用户账号体系（`loginType = "user"`）
  - 登录示例：`POST /api/user/login?username=xxx&password=xxx`，登录成功后会在响应头/Cookie 中携带 Token
- **角色与权限**：
  - 通过 `SysRoleEnum` 维护 `user / admin / super-admin` 三种角色，并在 `StpInterfaceImpl` 中实现角色继承和权限列表组装
  - 提供 `@SaUserCheckRole` 注解 + `SaUserAuthAspect` 切面，实现方法级角色拦截（支持 AND/OR 多角色模式）
  - 示例接口（见 `UserController`）：
    - `GET /api/user/role/user`：仅普通用户及以上可访问
    - `GET /api/user/role/admin`：仅管理员及以上可访问
    - `GET /api/user/role/super-admin`：仅超级管理员可访问
