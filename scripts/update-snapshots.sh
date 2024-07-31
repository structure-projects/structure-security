#!/usr/bin/env bash
# 更新快照版本
version=$1
if [ -z "$version" ]; then
    version=1.0.1-SNAPSHOT
fi
cd ../
cd structure-security-dependencies
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version
