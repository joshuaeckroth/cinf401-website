---
layout: post
title: Initial setup
---

# Initial setup

These notes detail procedures necessary to set up local and remote environments to use the tools discussed in this course.

Every student should follow these procedures once, at the beginning of the semester.

## Obtain login credentials and delenn IP address

We will use the school's "delenn" server for our work. You will also use your own computer.

Ask me for your login name and password, and delenn's IP address. delenn cannot be access with a domain name outside of the University. (Inside the University's network, it's `delenn.ad.stetson.edu`.) So, in order to access delenn from home, or wherever, you'll need the IP address from me.

## Configure SSH

delenn serves various web interfaces on specific ports. One of these (port 8080) is RStudio, the others are related to Hadoop. The University firewall does not allow us to access these ports directly, so we will tunnel them through SSH.

**You must connect to delenn via SSH before you can use RStudio or Hadoop web interfaces.** If you don't connect with SSH first, the ports will not be available.

Since we are using port forwarding, you will access delenn web interfaces by connecting to [localhost:8080](http://localhost:8080) or [localhost:50070](http://localhost:50070), etc.

RStudio uses port 8080. Hadoop services use ports 50070, 8088, 19888. See the [Hadoop dev env](/notes/hadoop-dev-env.html) notes for details.

### PuTTY (Windows)

![PuTTY tunneling](/images/putty-1.png)

![PuTTY tunneling](/images/putty-2.png)

### SSH (OS X, Linux)

Edit the file `~/.ssh/config` and add this to the bottom:

```
Host delenn
  HostName 1.2.3.4  <------ replace
  User jeckroth  <--------- replace
  LocalForward 8080 127.0.0.1:8080
  LocalForward 50070 127.0.0.1:50070
  LocalForward 8088 127.0.0.1:8088
  LocalForward 19888 127.0.0.1:19888
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