#!/usr/bin/env bash

git config --global user.name "Github Actions"
git config --global user.email "actions@github.com"

git init
git branch -m main
git remote add origin git@github.com:6-BennyLi-9/FtcStudio.git
git pull origin
git add org/betastudio/ftc/.
git add org/firstinspires/ftc/teamcode/.
git commit -m "auto update"
git push --set-upstream origin main