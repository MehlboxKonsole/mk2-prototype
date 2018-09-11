# MehlboxKonsole 2 - Prototype

[![Known Vulnerabilities](https://snyk.io/test/github/MehlboxKonsole/mk2-prototype/badge.svg)](https://snyk.io/test/github/MehlboxKonsole/mk2-prototype)
[![Build Status](https://travis-ci.org/MehlboxKonsole/mk2-prototype.svg?branch=master)](https://travis-ci.org/MehlboxKonsole/mk2-prototype)


This prototype is to give us a spike and will provide the following functions:

* Basic layout
* Log in with LDAP credentials
* Change password (LDAP backed)
* Show assigned e-mail addresses

Prerequisites
---
All you need is:
* Java 8
* Access to a LDAP server

We use a specific object class (_qmailUser_) on the Mehlbox. To make the set up as painless as possible a Docker
image exists with this object class already set up. See [Development LDAP server set up](#development-ldap-server-set-up)
below.

How to build and run
---
Building couldn't be easier:

1. Clone repository
2. Change into your fresh clone
3. Run: `./gradlew clean build`

And running the application is simply: `./gradlew bootRun` and open your browser on [http://localhost:8080](http://localhost:8080).

Keep in mind that you need a running LDAP server to be able to login. By default, the application is using
_ldap://localhost:389_ as LDAP connection URL. If your LDAP is listening on another address, use the following command:
```
JAVA_OPTS="-Dmk2.ldap.contextSource.url=ldap://<your-ldap-address>:<your-ldap-port>" ./gradlew bootRun
```

Alternatively you can export _JAVA_OPTS_ so you do not have repeat it every time:
```
export JAVA_OPTS="-Dmk2.ldap.contextSource.url=ldap://<your-ldap-address>:<your-ldap-port>"
```

To populate the LDAP, the easiest is to export from the main directory and import it into your local environment. Or ask
Holger to get a copy.

Development LDAP server set up
---
To get up to speed quickly a Docker image with SLAPD, already bundled with the _qmailUser_ object class, is waiting for
you. It is based on _dinkel/docker-openldap_ and supports a set of environment variables.
The most important variables needed are:

| Name                     | Description                                    | Example value |
|--------------------------|------------------------------------------------|---------------|
| SLAPD_DOMAIN             | Domain name you want to use for your base      | e-mehlbox.eu  |
| SLAPD_PASSWORD           | Password for the main directory                | root          |
| LDAP_CONFIG_PASSWORD     | Password for the _cn=config_ directory branch  | config        |
| SLAPD_ADDITIONAL_MODULES | Activates additional object classes. IMPORTANT | qmail         |

On Linux (and if I remember correctly on OSX as well), the following docker command will create a container named
_slapd_, listening on port 389.


```
docker run -itd --name slapd -e SLAPD_DOMAIN=e-mehlbox.eu -e SLAPD_PASSWORD=root -e SLAPD_CONFIG_PASSWORD=config -e SLAPD_ADDITIONAL_MODULES=qmail -p 389:389 daincredibleholg/docker-openldap
```

If you run docker within a virtual machine (e.g. Docker Machine), you can access the OpenLDAP instance via
_ldap://<docker-machine-address>:389_. If you run Docker natively on Linux, the URL is simply _ldap://localhost:389_.

