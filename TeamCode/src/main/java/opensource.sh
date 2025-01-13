#!/usr/bin/env bash

git config --global init.defaultBranch opensource
git config --global user.name "Github Actions"
git config --global user.email "actions@github.com"
cat ~/.git/config

git clone https://github.com/6-BennyLi-9/FtcStudio.git
git add org/betastudio/ftc/.
git add org/firstinspires/ftc/teamcode/.
git commit -m "auto update"
git push --set-upstream origin opensource