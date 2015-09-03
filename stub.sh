#!/usr/bin/env bash

STUB_DIR="mypodcasts-api-stub"

function _clone_or_pull () {
  root_dir=`pwd`
  if [ -d $1 ]; then
   echo "Prepare to pull the latest version of $1"
   cd $1
   git pull origin master
  else
   echo "Prepare to clone $1"
   git clone git@github.com:alabeduarte/$1.git $1
  fi
  pwd
  echo "Going back to $root_dir"
  cd $root_dir
}

_clone_or_pull $STUB_DIR

cd $STUB_DIR

npm install
npm start
