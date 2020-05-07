## SSO 登陆原理

假设现在有2个系统，app1.skrein.com app2.skrein.com
用于登陆app1.com 之后，访问app2.com无需登陆，可以直接访问。这个就叫单点登录。


## Cookie和Session

Cookie(维基百科):
> 指某些网站为了辨别用户身份而储存拉用户本地终端（Client Side）上个数据（通常经过加密）

可以理解为服务器将一些数据我们客户端，在浏览器每次请求cookie相同域的时候，浏览器会自动带上这个cookie。

Session（百度百科）：
> 当用户请求来自应用程序的 Web页时，如果该用户还没有会话，则Web服务器将自动创建一个 Session对象。当会话过期或被放弃后，服务器将终止该会话

> 当用户在应用程序的Web页之间跳转时，存储在Session对象中的变量将不会丢失

> Session对象存储特定用户会话所需的属性及配置信息

可以理解为 Session保持了与服务端的一个长连接，然后页面之间跳转变量不会丢失，可以存放一些配置。

## 如何实现一个SSO呢？

角色:

app1: `app1.skrein.com`
app2: `app2.skrein.com`
sso: `sso.skrein.com`

比如我们登陆app1的时候，系统发现没有登录，然后跳转到sso去登陆，登陆成功之后，我们只是对sso这个服务
建立了cookie和session的关系，然后回到aap1我们还是不知道到底我有没有登陆。

所以我们需要利用一个`ticket`的方式。

当我们通过sso登陆成功之后，在服务端生成一个`ticket`,于此同时生成一个`Cookie`发送给sso客户端。然后通过`Redirect`
的方式将这个`ticket`告诉app1,app1拿到这个`ticket`通过http的方式，去sso校验，我这个ticket是否是有效的。如果有效的
那么，app1服务端记录登陆状态。下次app1就可以不用登陆了。

回到app2,app2访问资源的时候，发现自己也没有登陆，然后跳转到sso，这时候访问sso的时候会带上app1之前与sso之前登陆之后，sso发给客户端的
cookie，然后sso服务端收到这个cookie发现已经登录过了，直接返回给app2一个ticket，然后app2就可以拿着ticket去sso校验。校验成功之后，
app2记录自己的登陆状态。

![sso登陆原理图](http://ww1.sinaimg.cn/large/005RZJcZgy1gek14gs063j30y61k4afn.jpg)

[大图链接](https://upload-images.jianshu.io/upload_images/12540413-041b3228c5e865e8.png)

原理和上面一模一样的。

### 代码效果

![1.PNG](http://ww1.sinaimg.cn/large/005RZJcZgy1gek1ctyji3j30dc06d3ym.jpg)

分别为App1，APP2,SSO-server


请求app1.skrien.com，发现没有登录直接跳转到sso.skrein.com/index/login
![2.PNG](http://ww1.sinaimg.cn/large/005RZJcZgy1gek1df57gyj30k106da9y.jpg)


输入用户名和密码 用户名为1 密码为2，登录成功，直接跳转到首页
![3.PNG](http://ww1.sinaimg.cn/large/005RZJcZgy1gek1e4wvq8j30c605ut8k.jpg)

再次访问app2.skrein.com，不用登陆，直接跳转
![4.PNG](http://ww1.sinaimg.cn/large/005RZJcZgy1gek1f7m4ghj30cn05b0sl.jpg)


### 代码执行需知

1. 修改 sso-server 

`com.skrein.sso.service.RedisService` Redis集群地址

2. 配置Nginx和Hosts

hosts

```text
127.0.0.1 app1.skrein.com
127.0.0.1 app2.skrein.com
127.0.0.1 sso.skrein.com
```


nginx.conf
```text
http{


 	upstream app1{
 	  server localhost:8111;
 	}

 	upstream app2{
 	  server localhost:8222;
 	}

 #虚拟主机1
 server{
  listen       80;  
  server_name  sso.skrein.com; 
  location / {     
	  proxy_pass http://127.0.0.1:8080;
	  proxy_set_header Host $http_host;
	  proxy_set_header X-Real-IP $remote_addr;
	  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
	  proxy_set_header X-Forwarded-Proto $scheme;
  }
 }

  server{
  listen       80;  
  server_name  app1.skrein.com; 
  location / {     
	  proxy_pass http://app1;
	  proxy_set_header Host $http_host;
	  proxy_set_header X-Real-IP $remote_addr;
	  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
	  proxy_set_header X-Forwarded-Proto $scheme;
	}
 }

 server{
  listen       80;  
  server_name  app2.skrein.com; 
  location / {     
	  proxy_pass http://app2;
	  proxy_set_header Host $http_host;
	  proxy_set_header X-Real-IP $remote_addr;
	  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
	  proxy_set_header X-Forwarded-Proto $scheme;
  }
 }

}
```

3. 修改端口号

app1:8111
app2:8222
sso-server:8080

然后访问： app1.skrein.com

### 转载说明

部分图片来自于 [牛初九](https://www.jianshu.com/p/75edcc05acfd) ，如有侵权，告知则删除。

### 代码地址

[sso-login](https://github.com/jounghu/sso-login) 一个乐于分享的程序员