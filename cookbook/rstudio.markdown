---
layout: post
title: "Cookbook: RStudio"
---

# Cookbook: RStudio

Get the URL from me. Login with your delenn credentials.

## Setup

- Tools > Global Options
    - Git/SVN
    - Create RSA key...
    - View public key
    - Copy, paste public key in Bitbucket: Click top-right avatar, Manage Account, left-side bottom SSH Keys, Add key

## Workflow

### New project

- Create repository on Bitbucket
    - Expand "I'm starting from scratch"
    - Copy git URL, e.g., `git@bitbucket.org:joshuaeckroth/cinf401-ex1.git`
- File > New Project...
    - Version Control
    - Git
    - Repository URL = copied git URL (e.g., `git@bitbucket.org:joshuaeckroth/cinf401-ex1.git`)
    - Create project as subdirectory of: (your choice, possibly create a subfolder in your home folder, to hold all your CINF 401 projects)
    - Finally, click Create Project
