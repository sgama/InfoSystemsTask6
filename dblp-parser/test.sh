DEBUG="debug"
if [ -z "$1" ]
then     
    MVN=mvn
else 
    if [ $1 == $DEBUG ]
    then
        MVN=mvnDebug
    else 
        MVN=mvn
    fi
fi

$MVN clean test -DforkMode=never
