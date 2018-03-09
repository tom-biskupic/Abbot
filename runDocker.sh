#!/bin/bash


docker run --name abbot -p8080:8080 --rm -v AbbotDB:/opt/Abbot --link abbot-mysql tombi/abbot
