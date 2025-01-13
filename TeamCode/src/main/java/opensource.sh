#!/usr/bin/env bash

git config --global user.email  "17826002105@163.com"
git config --global user.name   "MingYuan Li"

git init
git branch -m main
git remote add origin git@github.com:6-BennyLi-9/FtcStudio.git
git pull origin
git add org/betastudio/ftc/.
git add org/firstinspires/ftc/teamcode/.
git commit -m "auto update"
git push --set-upstream origin HEAD