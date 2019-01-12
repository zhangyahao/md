##检测商用后解决办法

1. 卸载teamviewer
2. 删除下面2个目录
   `C:\Program Files (x86)\TeamViewer`
   `C:\Users\Administrator\AppData\Roaming\TeamViewer`
3. 在注册表中删除下面的键值，如果没有就不用删除了。
    `HKEY_LOCAL_MACHINE\SOFEWARE\Wow6432Node\TeamViewer
     HKEY_CURRENT_USERS\Software\TeamViewer
     HKEY_LOCAL_MACHINE\SOFTWARE\TeamViewer`   
4.  修改网卡地址


5. 如果为mac
    [解决办法](https://zhuanlan.zhihu.com/p/46180174)