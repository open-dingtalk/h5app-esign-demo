# esign-demo

> 电子签名API是e签宝提供的一种电子签名开放服务能力，e签宝提供了丰富的API接口，为用户提供一套完整的全生态电子签名服务，为客户提供具有法律效力的电子合同全生命周期服务，将原本需要耗费数日之久的文件签署环节，压缩到只需几十秒，降本增效。
>
> 包含功能：

- 服务商数据初始化：调用接口帮助钉钉ISV服务商进行e签宝开放平台的数据初始化。所有e签宝接口在调用前，都需要成为e签宝的开发者，基于ISV服务商数据已完成初始化
- 企业授权：调用接口获取企业授权的页面地址，然后企业授权使用钉签开放平台提供的功能
- 上传文件：调用接口获取到文件上传地址，然后上传要签署的文件到改地址

- 发起签署流程：调用接口发起签署流程
- 获取流程任务的所有合同列表：获取流程任务的所有合同列表，收到签署完成消息后查询

## Getting Started

### 克隆代码仓库到本地

git clone

```
https://github.com/open-dingtalk/h5app-esign-demo.git
```

### 开发环境准备

#### 钉钉开放平台环境准备

1. 需要有一个钉钉注册企业，如果没有可以创建：https://oa.dingtalk.com/register_new.htm?source=1008_OA&lwfrom=2018122711522903000&succJump=oa#/

2. 成为钉钉开发者，参考文档：https://developers.dingtalk.com/document/app/become-a-dingtalk-developer

3. 登录钉钉开放平台后台创建一个第三方企业应用： https://developers.dingtalk.com/document/app/create-isvapp

4. 配置应用

   配置开发管理，参考文档：https://developers.dingtalk.com/document/app/isvapp-development-overview

   ![image-20210719104930401](https://img.alicdn.com/imgextra/i4/O1CN014WgSV71CO6Q4rl2h6_!!6000000000070-2-tps-2878-1578.png)

   配置免登相关权限：https://developers.dingtalk.com/document/app/address-book-permissions

   ![image-20210719105409038](https://img.alicdn.com/imgextra/i1/O1CN01QJWA251xd1GA06mTD_!!6000000006465-2-tps-2878-1588.png)

   配置e签宝数据管理权限：

   ![image-20210719105409039](https://img.alicdn.com/imgextra/i3/O1CN01qBL68C1yFwwOdLjFd_!!6000000006550-2-tps-2822-1014.png)
   
   添加体验组织进行授权：https://developers.dingtalk.com/document/app/publish-and-install-isvapp

   ![image-20210719105555840](https://img.alicdn.com/imgextra/i4/O1CN015zvwqo1WDNSZzfk4O_!!6000000002754-2-tps-2878-1520.png)

   配置文件参数：suiteKey、suiteSecret、corpId、aesKey、token、suiteTicket

   ![image-20210719110043495](https://img.alicdn.com/imgextra/i4/O1CN01gkC6BU1thMYGP2sIk_!!6000000005933-2-tps-2878-1382.png)

   ![image20210701133842908](https://img.alicdn.com/imgextra/i4/O1CN01Ns2Oey1xPHN6Z870f_!!6000000006435-2-tps-2856-1076.png)

   ![image-20210719110817685](https://img.alicdn.com/imgextra/i2/O1CN01DONq29228rFnRJtwT_!!6000000007076-2-tps-2874-1554.png)

#### 使用命令行安装依赖&打包

```txt
cd fronted/
```

![image-20210719112307604](https://img.alicdn.com/imgextra/i3/O1CN013sn31S1VtgQfUQZMH_!!6000000002711-2-tps-2872-640.png)

```txt
npm install
```

![image-20210719112611587](https://img.alicdn.com/imgextra/i4/O1CN01p65cg61TwlroU50To_!!6000000002447-2-tps-1952-1068.png)

```txt
npm run build
```

![image-20210719112750140](https://img.alicdn.com/imgextra/i3/O1CN01GUIjIb1YIX9IyWjeu_!!6000000003036-2-tps-1904-868.png)

#### 将打包好的静态资源文件放入后端服务

![image-20210719112847521](https://img.alicdn.com/imgextra/i4/O1CN01mn1FPu1SHGMBplpRw_!!6000000002221-2-tps-1944-1104.png)

#### 修改服务端配置文件

![image-20210719112950225](https://img.alicdn.com/imgextra/i4/O1CN01Y2sejp27k1TCmDENg_!!6000000007834-2-tps-1924-918.png)

### 参考文档

1. 创建第三方企业应用，文档链接：https://developers.dingtalk.com/document/app/isvapp-development-process
2. 安装e签宝微应用、企业实名认证，文档链接：https://developers.dingtalk.com/document/app/esign-overview
3. e签宝通知回调，文档链接：https://developers.dingtalk.com/document/app/notification-callback
4. e签宝初始化，文档链接：https://developers.dingtalk.com/document/app/isv-service-provider-data-initialization
5. e签宝授权，文档链接：https://developers.dingtalk.com/document/app/obtain-the-address-of-the-authorized-page
6. 上传文件，文档链接：https://developers.dingtalk.com/document/app/obtain-the-upload-url-of-a-file-1，https://developers.dingtalk.com/document/app/file-stream-upload
7. 创建签署流程，文档链接：https://developers.dingtalk.com/document/app/use-the-api-to-initiate-a-signature-process
8. 查询企业信息，文档链接：https://developers.dingtalk.com/document/app/query-enterprise-information
9. 查询个人信息，文档链接：https://developers.dingtalk.com/document/app/query-personal-information
10. 获取流程任务所有合同列表，文档链接：https://developers.dingtalk.com/document/app/get-a-list-of-all-contracts-for-the-process-task
