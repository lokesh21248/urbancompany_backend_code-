@echo off
REM Maven wrapper script for Urban Services Backend
REM Downloads and runs Spring Boot backend

set MAVEN_HOME=C:\maven\apache-maven-3.9.6
set JAVA_HOME=C:\Program Files\Microsoft\jdk-17.0.18.8-hotspot

"%MAVEN_HOME%\bin\mvn.cmd" %*
