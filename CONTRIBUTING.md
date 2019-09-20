# Contributing to Camunda Kafka Polling Client

Most important first: Thank you for your contribution! We are happy to hear that and try to make this as efficient and hassle free as we possibly can.

This document is intended to give contributors a set of guidelines for the contribution.
The guidelines should provide help for your contribution. 
Feel free to suggest changes or enhancements to this guideline.

# How can I contribute?
1. Let's talk about it. Preferrably in the form of a github issue.
2. Sign the [Contributor License Agreement](CLA_individual.md) and send it via e-mail to one of the maintainers of the project (we store CLAs separately for GDPR reasons). There is a separate form for those, [contributing as a corporate entity](CLA_corporate.md) and not as an individual.
3. Fork this repository.
4. Push your changes to a topic branch in your fork of the repository and make sure that sonar is happy with it (i.e. the quality gate holds).
5. Submit a pull request - we will get back to you shortly.

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