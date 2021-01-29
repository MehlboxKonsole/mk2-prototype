#!/bin/sh -x

cd /mk2/

# Create application.properties
LDAP_HOST=${LDAP_HOST:-'ldap:\/\/localhost:389'}
LDAP_USERNAME=${LDAP_USERNAME:-'cn=admin'}
LDAP_PASSWORD=${LDAP_PASSWORD:-'root'}
LDAP_BASE=${LDAB_BASE:-"dc=e-mehlbox,dc=eu"}

sed -i "s/LDAP_URLS/${LDAP_HOST}/" config/application.yml
sed -i "s/LDAP_USERNAME/${LDAP_USERNAME}/" config/application.yml
sed -i "s/LDAP_PASSWORD/${LDAP_PASSWORD}/" config/application.yml
sed -i "s/LDAP_BASE/${LDAP_BASE}/" config/application.yml

java -server -jar app.jar