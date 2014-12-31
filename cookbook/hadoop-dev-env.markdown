---
layout: post
title: "Cookbook: Hadoop dev environment"
---

# Cookbook: Hadoop dev environment

![Network diagram](/images/network-diagram.png)

## SSH configurations

Web interfaces:

- Port 50070
- Port 8088
- Port 19888

These are the default ports with an Apache Hadoop installation.

### PuTTY (Windows)

### SSH (OS X, Linux)

```
Host delenn
  HostName 1.2.3.4  <------ replace
  User jeckroth  <--------- replace
  LocalForward 8080 127.0.0.1:8080
  LocalForward 3142 127.0.0.1:3142
  LocalForward 50070 192.168.121.13:50070
  LocalForward 8088 192.168.121.194:8088
  LocalForward 19888 192.168.121.194:19888
```



