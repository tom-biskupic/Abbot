[![Build Status](https://travis-ci.com/tom-biskupic/Abbot.svg?branch=master)](https://travis-ci.com/tom-biskupic/Abbot)

# Abbot

Abbot is a a web application for managing sailing results. It was written to meet the specific needs of the Abbotsford 12 foot sailing club but could be used for other dinghy sailing races.

The application allows you to:
* Manage multiple race series where each series has a set of registered competitors, competitions and races
* Run races where a mixed fleet of vessels are competing and their results are combined using the yardstick of each class of vessel.
* Confgure competitions on scratch or handicap results and use low-points or bonus points system.
* Have a race that contributes to multiple competitions - for example the same race could contribute to a season point score as well as a championship (scratch) competition.
* Adjust handicaps based on the Mark Foy 3-2-1 handicap system with the additional 3rd win rule. (More handicap systems to follow).

# How it works

You can configure a set of boat classes for each race series and then you can register boats of those classes. Classes can have divisions to allow for sub-groups such as Laser Radial or Laser Standard etc.

Boats compete as part of a fleet. A fleet is a list of boat classes/divisions that will race together (start together). For example you might have a fleet which is just a single class of vessel (Laser) or you might define a fleet that is compose of multiple vessels. For example you might have a 'Juniors' fleet that encompasses Optimist, Sabots and other junior sailing vessels or you might want an 'Open' class that covers any class of vessel where you don't have enough competitors to form a a class based fleet.

A competition defines how you combine the results from multiple races. Each competition is associated with a fleet and defines the type of result to use (handicap or scratch), the points system and the number of discards.

You create races for each fleet and you can associate one or more competitions with that race. Each race is configured to occur on a specific date and you can name individual races for sponsorship or other reasons.

You enter results for each race by selecting a competitor, selecting their finish status and then their start and finish time. When entering multiple results the start time defaults to the last start time entered. You can have races where not all competitors in the fleet start at the same time by just entering different start times. Once you have entered all the results you can update the race status to complete and optionally add results for boats that didn't start. In addition you can update the handicaps for all the competitors based on the results of that race.

You can view the points tables for each competition online plus there is an export facility that allows you to export either race data (boat name, start, finish elapsed, corrected time etc) or points tables as HTML to download. This can be used to upload to a club website for publication.

# Running Abbot

Abbot is a Java/Spring/AngularJS/Bootstrap application that uses MySQL for database storage.

To run a demo you can use the docker target, then the runMysql.sh script to startup a MySQL and runDocker.sh to start Abbot in a docker. Navigate to http://localhost:8080/Abbot3 to access the application. Then register a user account, logon and create a race series.

The user registration is pretty basic at the moment and does not validate your email address.
