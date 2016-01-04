@echo off
mvn install:install-file -Dfile=NodeBase.jar -DgroupId=iscas.distributed -DartifactId=NodeBase -Dversion=1.0 -Dpackaging=jar
pause