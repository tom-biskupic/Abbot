#!/bin/bash
mkdir -p ./dev-certs
keytool -genkeypair -alias abbot -keyalg RSA -keysize 4096 \
  -validity 3650 -dname "CN=localhost" -keypass changeit -keystore ./dev-certs/keystore.p12 \
  -storeType PKCS12 -storepass changeit

