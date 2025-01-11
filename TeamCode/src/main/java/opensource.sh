#!/usr/bin/env bash

remote_repo_url=github.com

git init
git remote add origin $remote_repo_url
git pull origin
git add org/betastudio/ftc/.
git add org/firstinspires/ftc/teamcode/.
git commit -m "auto update"
git push origin