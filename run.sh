#!/bin/bash -x
#
# run.sh
#

JVM_MEM_PARAMS="-Xmx1G"

if [ "$(uname)" != "Darwin" ]; then
    JAVA_HOME=/opt/java8
    MAX_MEM=`cat /proc/meminfo | grep MemTotal | perl -n -e 's/.* (\d+) .*/\1/; print int($_ *.70 / 1024)'`
    MIN_MEM=`cat /proc/meminfo | grep MemTotal | perl -n -e 's/.* (\d+) .*/\1/; print int($_ *.50 / 1024)'`
    JVM_MEM_PARAMS="-Xms${MIN_MEM}m -Xmx${MAX_MEM}m"
fi

DSN="-DSENTRY_DSN=http://bsconsumer:bsconsyu@sentry/XXXX"

JAVA_OPTS="${DSN} -Djava.net.preferIPv4Stack=true -XX:+UseBiasedLocking -XX:+AggressiveOpts -XX:+UseFastAccessorMethods ${JVM_MEM_PARAMS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/deployment"

JAR=$(find ./ -name weibird-*-fat.jar | tail -1)

exec $JAVA_HOME/bin/java -XX:OnOutOfMemoryError="sleep 60 && kill -9 %p" ${JAVA_OPTS} -jar ${JAR} ${OPTS}
echo Done
