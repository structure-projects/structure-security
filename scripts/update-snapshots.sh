#!/usr/bin/env bash
source .env
# 更新快照版本
version=$1
if [ -z "$version" ]; then
    version=${APP_VERSION}-SNAPSHOT
fi
cd ../
cd structure-security-dependencies
mvn clean deploy -P release,gpg -Dmaven.test.skip=true -Drevision=$version
