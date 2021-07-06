#!/bin/bash
echo "#!/bin/bash" > ./backend/start.sh

if [ ! -n "$1" ]; then
  APP_NAME="demo"
else
  APP_NAME=$1
fi

index=0
for item in $*
do
  let index+=1
  if [ $index -ge 2 ]; then
    echo "export ${item}" >> ./backend/start.sh
  fi
done

echo "java -Dserver.port=8080 -jar ${APP_NAME}.jar" >> ./backend/start.sh