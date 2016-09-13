#!/bin/sh

cygwin=false
case "$(uname)" in
    CYGWIN*)
        cygwin=true
        ;;
esac

get_pid() {
    STR=$1
    PID=$2
    if $cygwin ; then
        JAVA_CMD="$JAVA_HOME/bin/java"
        JAVA_CMD=$(cygpath --path --unix $JAVA_CMD)
        JAVA_PID=$(ps | grep $JAVA_CMD | awk '{print $1}')
    else
        if [ ! -z "$PID" ] ; then
            JAVA_PID=$(ps -C java -f --width 1000 | grep "$STR" | grep "$PID" -v grep | awk '{print $2}')
        else
            JAVA_PID=$(ps -C java -f --width 1000 | grep "$STR" | grep -v grep | awk '{print $2}')
        fi
    fi
    echo $JAVA_PID;
}

base=$(dirname $0)/..
pid_file=$base/bin/JProxyTest.pid
if [ ! -f "$pid_file" ] ; then
    echo "JProxyTest is not running. exists"
    exit
fi

pid=$(cat $pid_file)
if [ "$pid" == "" ] ; then
    pid=$(get_pid "appName=jproxy-test")
fi

echo -e "stopping jproxy-test $pid ... "
kill -9 $pid

LOOPS=0
while (true);
do
    gpid=$(get_pid "$pid_file" "$pid")
    if [ "$gpid" == "" ] ; then
        echo "stop successful"
        $(rm $pid_file)
        break;
    fi
    let LOOPS=LOOPS+1
    sleep 1
done