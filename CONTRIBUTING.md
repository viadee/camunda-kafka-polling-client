# Contributing to Camunda Kafka Polling Client

Most important first: Thank you for your contribution!

This document is intended to give contributors a set of guidelines for the contribution.
The guidelines should provide help for your contribution. 
Feel free to suggest changes or enhancements to this guideline.

# How can I contribute?

## File a bug
If you find a bug, feel free to submit an issue.

To be able to understand the bug, please attach additional information to enable reproducing the bug.
Especially the used version of the polling client, used polling method (rest or jdbc) and used version of Camunda BPM
will help to understand the bug.

## Create a pull request
If you already have a suggested bugfix or an enhancement, you can also provide a pull request.
In case of a bugfix, the pull request should include information about the fixed bug.
In case of an enhancement, the pull request should include a description about the intended extension.

# Styleguide
There is a set of Eclipse code formatting rules available as `code-formatter.xml` in the project root.
These rules can be imported into Eclipse or IntelliJ. There is also a maven plugin configured, which can
format the project source code based on these formatting rules by using `mvn formatter:format`.

The maven plugin is also configured to validate source code is formatted using these rules during `verify` phase 
of the build.