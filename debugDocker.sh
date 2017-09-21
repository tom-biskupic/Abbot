#!/bin/bash


docker run -p8080:8080 -p8000:8000 --rm -v AbbotDB:/opt/Abbot --entrypoint "catalina.sh" tombi/abbot jpda run
