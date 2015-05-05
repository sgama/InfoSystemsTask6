#XML Location
XML_FILE='dblp_filtered.xml'

#If this is set to true, the port 5005 will be open for debugging
DEBUG=false

PRINT_GC_OPTION='-XX:+PrintGC'

#Java options
# -DentityExpansionLimit    - needs to be set quite high because of the xml size
# -Xmx4G                    - memory limit
JAVA_OPTS='-Xmx3G -XX:+UseConcMarkSweepGC -DentityExpansionLimit=10000000'
DEBUG_OPTS='-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005'
if $DEBUG ; then
    JAVA_OPTS=$JAVA_OPTS" "$DEBUG_OPTS
fi

java -jar $JAVA_OPTS './dblp-parser/target/dblp-parser-1.0.0.jar' $XML_FILE

#usage java -jar -Xmx1G -XX:+UseConcMarkSweepGC -XX:+PrintGC -DentityExpansionLimit=10000000 ./dblp-parser/target/dblp-parser-1.0.0.jar <path to xml>
