# Getting up and running

Since we do not have a SUT (SystemUnderTest), we can use two simulator instances to be able to test all the scenarios implemented within this project.

## Start the simulators from the command line

CoolTel:
```
$ java -jar target/wbci-simulator-0.0.1-SNAPSHOT-exec.jar --server.port=8080  --partner.carrier.port=9090 --info.simulator.name='PortingSimulator for CoolTelProvider'
```

MyTel
```
$ java -jar target/wbci-simulator-0.0.1-SNAPSHOT-exec.jar --server.port=9090  --partner.carrier.port=8080 --info.simulator.name='PortingSimulator for MyTelProvider'
```

## Open in your browser
[CoolTel-PortingSimulator](http://localhost:8080/)

[MyTel-PortingSimulator](http://localhost:9090/)
