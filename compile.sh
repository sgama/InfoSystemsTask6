rm -rf $HOME/.m2/repository/ch/ethz

DEFAULT_BACKEND=db4o
BACKEND=$1
if [ -z "$BACKEND" ]
then 
    BACKEND=$DEFAULT_BACKEND
fi

#Compile everything
mvn clean package -Dbackend=$BACKEND
