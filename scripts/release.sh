#!/usr/bin/env bash
source .env
#发行新的版本上传到中心仓库可以执行这个脚本
#!/bin/bash
version=$1
if [ -z "$version" ]; then
    version=${APP_VERSION}
fi
echo "version: $version"
cd ../
cd structure-security-dependencies
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version
