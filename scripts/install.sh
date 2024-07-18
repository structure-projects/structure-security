#!/usr/bin/env bash
#在本地仓库安装.RELEASE
version=$1
if [ -z "$version" ]; then
    version=1.0.1
fi
cd ../
cd structure-security-dependencies
mvn clean install -Dmaven.test.skip=true -Drevision=$version
