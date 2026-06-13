#!/usr/bin/env bash
source .env
#在本地仓库安装.RELEASE
version=$1
if [ -z "$version" ]; then
    version=${APP_VERSION}
fi
echo "version: $version"
cd ../
cd structure-security-dependencies
mvn clean install -Dmaven.test.skip=true -Drevision=$version
