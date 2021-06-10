解决办法，显式升级nacos. nacos-client 1.4.1 存在严重的 bug，客户端与 Nacos Server 如果发生短暂的域名解析问题，会导致心跳永久丢失，进而引发服务全量下线，即使网络恢复，也不会自动恢复心跳。

域名解析失败常见于网络抖动或者 K8s 环境下的 coreDNS 访问超时等场景，为避免域名解析对 Nacos 造成的重大影响，请务必自查应用代码中使用的 nacos-client 的版本。

该问题仅存在于 1.4.1 版本，低于此版本不受此问题的影响，使用 1.4.1 的用户建议升级至 1.4.2 以避免此问题。

使用 SpringCloud/Dubbo 的用户，需要确认实际框架使用的 nacos-client 版本，可以通过显式指定 nacos-client 的版本以覆盖框架默认的版本。其中 Dubbo 用户要格外小心，Dubbo 的 2.7.11
版本默认使用了 nacos-client 1.4.1，务必显式指定 nacos-client 的版本到 1.4.2，Dubbo 也将在下个 release 版本替换 Nacos 的默认版本。

[原文](https://mp.weixin.qq.com/s/wXfwxUFTP1WOWrtOCMNRGw)
