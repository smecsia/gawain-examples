#!/bin/bash

cat <<-JS > /tmp/mongoinit.js
    rs.initiate({"_id":"gawain","members":[
        {"_id":1,"host":"mongodb.dc:27017"}
    ]});
JS

sleep 5
/usr/bin/mongo --host mongodb.dc /tmp/mongoinit.js
sleep 69696969
