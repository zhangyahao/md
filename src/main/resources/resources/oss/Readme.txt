AccessKeyID 
AccessKeySecret
RoleArn
TokenExpireTime
PolicyFile
Region：
RoleSessionName：是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁


需要在RAM控制台获取，此时要给子账号权限，并建立一个角色，把这个角色赋给子账户，这个角色会有一串值，就是rolearn要填的
记得角色的权限，子账户的权限要分配好，不然会报错

浏览器 -> JavaWeb服务器 -> 浏览器获得授权，直接上传到OSS云端