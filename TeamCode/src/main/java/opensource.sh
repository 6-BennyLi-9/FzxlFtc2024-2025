#!/usr/bin/env bash

git config --global init.defaultBranch opensource
git config --global user.name "Github Actions"
git config --global user.email "actions@github.com"
cat ~/.git/config

git init
git remote add origin https://github.com/6-BennyLi-9/F
git branch -m opensource
git add org/betastudio/ftc/.
git add org/firstinspires/ftc/teamcode/.
git commit -m "auto update"
git push --set-upstream origin opensource