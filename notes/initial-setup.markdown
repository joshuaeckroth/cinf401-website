---
layout: post
title: Initial setup
---

# Initial setup

These notes detail procedures necessary to set up local and remote environments to use the tools discussed in this course.

Every student should follow these procedures once, at the beginning of the semester.

## Connect to delenn

We will use the school's "delenn" server for our work.

Ask me for your login name and password. The address for SSH is `delenn.stetson.edu` and the port is 2222. **Take note of the port.**

### Off-campus access

If you are off campus, you cannot connect to delenn with Putty/SSH until you first connect to the VPN.

Download [FortiClient](http://www.forticlient.com/downloads). Configure it as an SSL VPN, IP address 147.253.200.11, port 443, and your Stetson username (e.g., jeckroth or dplante) and Stetson password (not delenn password!).

![FortiClient VPN](/images/forticlient.png)

### Configure SSH

delenn serves various web interfaces on specific ports. Port 8080 is RStudio, while 9000 will be used for Hadoop services. These ports will only be accessible via a tunnel through SSH.

**You must connect to delenn via SSH before you can use RStudio or Hadoop web interfaces.** If you don't connect with SSH first, the ports will not be available.

Since we are using port forwarding, you will access delenn web interfaces by connecting to [localhost:8080](http://localhost:8080) for RStudio or [localhost:9000](http://localhost:9000) for Hadoop services.

### PuTTY (Windows)

Port forwarding for ports 8080 and 9000:

![PuTTY tunneling](/images/putty-2.png)

### SSH (OS X, Linux)

Edit the file `~/.ssh/config` and add this to the bottom:

```
Host delenn
  HostName delenn.stetson.edu
  User jeckroth  <--------- replace
  Port 2222
  LocalForward 8080 127.0.0.1:8080   # RStudio: http://127.0.0.1:8080/
  LocalForward 9000 127.0.0.1:9000   # Hadoop web interface: http://127.0.0.1:9000/hadoop/
```

Now you can connect in the terminal with the command `ssh delenn`

## Create a Bitbucket account

If you have not already, create a free [Bitbucket](https://bitbucket.org) account.

## Configure git

You’ll need to tell git your name and email. Every commit is recorded with a name and email. You should use the same email address you used when you created a Bitbucket account.

Log into delenn via SSH. Execute these commands (**use your own name and email!**):

```
git config --global user.name "Joshua Eckroth"  <--- change to your name
git config --global user.email "jeckroth@stetson.edu"  <--- change to your email
```

## Configure RStudio & Bitbucket

After configuring SSH (see above) and logging in (so that the tunneling is active), visit [http://localhost:8080](http://localhost:8080) to access RStudio. Login with your delenn credentials.

![Configure RStudio](/images/setup-rstudio-1.png)

We'll need to set up an SSH key to use Bitbucket. We can do this from RStudio (which is cool!). In the Tools menu, click "Global Options..."

![Configure RStudio](/images/setup-rstudio-2.png)

Click "Git/SVN" on the left menu.

![Configure RStudio](/images/setup-rstudio-3.png)

Click "Create RSA Key..."

![Configure RStudio](/images/setup-rstudio-4.png)

If desired, provide a password. Then click "Create".

![Configure RStudio](/images/setup-rstudio-5.png)

The public/private key pair will be generated. Click "Close".

![Configure RStudio](/images/setup-rstudio-6.png)

Next, we need to tell Bitbucket about your public key. Click "View public key".

![Configure RStudio](/images/setup-rstudio-7.png)

Copy the public key. We'll soon paste it on the Bitbucket website.

![Configure RStudio](/images/setup-rstudio-8.png)

Now, go to [Bitbucket](https://bitbucket.org), and click your avatar on the top-right, then click "Manage account".

![Configure RStudio](/images/setup-rstudio-9.png)

Click "SSH keys" on the left menu.

![Configure RStudio](/images/setup-rstudio-10.png)

Click "Add key".

![Configure RStudio](/images/setup-rstudio-11.png)

Paste your public key (copied earlier) and click "Add key".

![Configure RStudio](/images/setup-rstudio-12.png)

Now you should be ready to follow the [RStudio workflow](/notes/rstudio-workflow.html).
