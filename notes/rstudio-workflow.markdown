---
layout: post
title: RStudio workflow
---

# RStudio workflow

These notes describe a typical workflow for R-based assignments and group projects. Also see the [Hadoop workflow](/notes/hadoop-workflow.html) notes. Be sure to follow the [initial setup](/notes/initial-setup.html) notes if you have not already.

## Log into Bitbucket, create a repository

1. Go to [https://bitbucket.org](https://bitbucket.org/) and log in.
2. Create a new repository with the "Create" button on the top menu.
3. Give it a name exactly like "cinf401-assignment-1" or "cinf401-group-project-1", changing the "1" to the correct number. You must name your repository exactly as described so I can automatically clone and grade your work.
4. Ensure the "private repository" checkbox is checked, and that the repository type is "git".

![Bitbucket create](/images/workflow-bitbucket-create-repository.png)

Add me (joshuaeckroth) as a "reader" of this private repository. To do so, click the gear icon on the left.

![Bitbucket config](/images/workflow-bitbucket-config.png)

Then click "Access Management".

![Bitbucket access management](/images/workflow-bitbucket-access-management.png)

Then add me (joshuaeckroth) as a reader.

![Bitbucket reader access](/images/workflow-bitbucket-reader-access.png)

Click the top left bucket icon to get back to the main view.

![Bitbucket bucket icon](/images/workflow-bitbucket-bucket-icon.png)

Expand the "I'm starting from scratch" option and copy the repository URL. It looks like `git@bitbucket.org:joshuaeckroth/cinf401-assignment-1.git`

![Bitbucket repository URL](/images/workflow-bitbucket-repository-url.png)

## Log into RStudio

1. Connect to delenn with SSH. You should have already forwarded localhost port 8080 to delenn's localhost port 8080 from the [initial setup notes](/notes/initial-setup.html).
2. Open your browser to [http://localhost:8080](http://localhost:8080/).

![RStudio login](/images/workflow-1.png)

## Create a new git project

Select menu item File > New Project... and choose Version Control.

![RStudio new project](/images/workflow-2.png)

Next, select git.

![RStudio git project](/images/workflow-3.png)

Paste your Bitbucket repository URL (from above, it is something like `git@bitbucket.org:joshuaeckroth/cinf401-assignment-1.git`). Give your project a name exactly like "cinf401-assignment-1" or "cinf401-group-project-1". If desired, place your project in a certain subdirectory of your home directory on delenn.

![RStudio clone git repository](/images/workflow-4.png)

Assuming it was successful, you should see several mentions of your project name and directory (see ovals in graphic).

![RStudio successful clone](/images/workflow-5.png)

## Create a new R Markdown file

You'll write your prose and final code in Markdown format (or LaTeX if you prefer; adjust these steps accordingly). To do so, select menu item File > New File > R Markdown...

![RStudio new R Markdown](/images/workflow-6.png)

Name this file exactly "cinf401-assignment-1" or "cinf401-group-project-1" (change the "1" as appropriate). Ensure your full name is provided in the "Author" textbox. Select HTML for the output format (this can be changed later; HTML is easiest to preview).

![RStudio new R Markdown 2](/images/workflow-7.png)

After clicking "Ok", you should see a new left-pane open with default R Markdown text. You will delete most of this text (starting after the second `---`) and begin your work.

![RStudio new R Markdown 3](/images/workflow-8.png)

## Save your new R Markdown file

I suggest you immediately save this R Markdown file. Choose a file name exactly like "cinf401-assignment-1" or "cinf401-group-project-1".

![RStudio save file](/images/workflow-9.png)

After clicking "Save", you should see the filename mentioned in various locations.

![RStudio save file 2](/images/workflow-10.png)

## Make your initial git commit and push

I also suggest you immediately commit and push your new files. Your git repository is currently still empty. To add, commit, and push the files you created, follow these steps.

1. Click the "Git" tab on the top right pane.
2. Select the checkboxes next to each of the files in the project. (You won't always want to add every file in your project. But in this case we do.) Notice the green "A" ("Added") in the Status column of these files.
3. Click "Commit".

![RStudio add new files to repository](/images/workflow-11.png)

A new window will pop up. (If it doesn't, check your browser pop-up blocking settings.) Type a commit message in this window and click "Commit".

![RStudio commit](/images/workflow-12.png)

A text box will (hopefully) indicate success. Close this commit window.

![RStudio commit success](/images/workflow-13.png)

Now, in the top-right pane, you should see that the added files are missing. This is because the git repository has up to date records of these files, so they are considered "unchanged" and thus won't appear here.

Click the green up arrow to "push" your local commits to Bitbucket.

![RStudio push commits](/images/workflow-14.png)

You should see a message indicating success.

![RStudio push success](/images/workflow-15.png)

If desired, you can navigate to Bitbucket and view the files/commits you just pushed. Refresh your Bitbucket repository page, then click one of the two buttons indicated in the graphic below. The top button shows the files in the "HEAD" branch of the repository, and the bottom button shows the history of commits.

![Bitbucket overview](/images/workflow-16.png)

Here is what you see if you click the top button.

![Bitbucket source](/images/workflow-17.png)

Here is what you see if you click the bottom button.

![Bitbucket commits](/images/workflow-18.png)

## Make some changes, commit and push

After working on your files and clicking the save button, you'll notice that the top-right pane, in the "Git" tab, shows the changed files.

![RStudio changed files](/images/workflow-19.png)

If so inclined, select a file and choose the "Diff" action.

![RStudio diff](/images/workflow-20.png)

This view shows how the current state of the (saved) file differs from the prior commit.

![RStudio diff 2](/images/workflow-21.png)

From this view, you can add the file to be commited, type a commit message, and perform the commit.

![RStudio commit](/images/workflow-22.png)

You should see that the file was committed.

![RStudio commit success](/images/workflow-23.png)

Now the Git tab shows no changed files, and also indicates that you have one commit that has not been pushed to Bitbucket ("origin").

![RStudio unpushed commits](/images/workflow-24.png)

## Render your R Markdown file

Finally, to preview your report, typed into an R Markdown file, just click the "Knit HTML" button. You can render to PDF or Microsoft Word, instead, by clicking the button's down-arrow.

![RStudio Knit HTML](/images/workflow-25.png)

Here is the HTML output. Note that the HTML file contains the image data inside the file, so this single HTML file can be sent to others and they will see all your graphics.

![knitr HTML output](/images/workflow-26.png)

Here is the PDF output.

![knitr PDF output](/images/workflow-27.png)

Here is the Microsoft Word output.

![knitr Microsoft Word output](/images/workflow-28.png)

Note that "knitting" creates files in your project directory, so you can add them to your git repository. This is not necessary for turning in your assignment/group projects, however, since I will "knit" the source file myself.

![RStudio knitr outputs](/images/workflow-29.png)

## Ask for help

RStudio has some nice help facilities. For R Markdown help, click the "?" button (next to the "Knit HTML" button).


![RStudio R Markdown help](/images/workflow-30.png)

Choosing "Markdown Quick Reference" will activate the bottom-right pane and switch to the "Help" tab.


![RStudio R Markdown help 2](/images/workflow-31.png)

This Help tab is useful for a wide variety of R docs. Click the home button to start from the top, or use the search box to look up particular functions, e.g., the `merge` function.

![RStudio Help tab](/images/workflow-32.png)