#!/usr/bin/env bash
#mkdir /home/tl/.ssh
#exec gosu tl:tlgroup java -server ${JAVA_GC} -Xms512m -Xmx512m ${JAVA_OPTS} \
exec java -server ${JAVA_GC} -Xms512m -Xmx512m ${JAVA_OPTS} \
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:28964 \
    -cp /app/resources:/app/classes:/app/libs/* \
	${MAIN_CLASS}
