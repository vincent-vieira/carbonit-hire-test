# CarbonIT Hiring Test [![Build Status](https://travis-ci.org/vincent-vieira/carbonit-hiring-test.svg?branch=master)](https://travis-ci.org/vincent-vieira/carbonit-hiring-test)
## About the project
This project is a Java 8 based exercise, about adventure and mountains.

## How do I commit ?
All my commits follow the [AngularJS commit message guidelines](https://github.com/angular/angular.js/blob/master/CONTRIBUTING.md#commit),
and the project is fully aware of the [Git Flow](http://danielkummer.github.io/git-flow-cheatsheet/index.fr_FR.html) methodology.
Each feature is located into a separate branch, then merged into `develop` when all tests pass and that the feature the branch represents has been fully completed.
When a release is about to happen, then the `develop` branch is merged into `origin/master` and the merge is tagged with the version number.

## Continuous integration
This project is fully integrated into [TravisCI](https://travis-ci.org) (**as you can see on the small image present
at the right of the title**), and the CI job is configured to run all unit tests using Java 8.

## Playing with the project
### Running/testing
Just run the `package` Maven goal to :
- Compile sources
- Trigger tests
- Generate the Javadoc
- Generate all code coverages reports

_All docs will be present in the `docs` subfolder of the `${project.basedir}` folder._

_All coverage reports will be present in the `coverage` subfolder of the `${project.basedir}` folder._

If you just want to launch the program, use the `exec:java` goal with the `exec.mainClass` property set, pointing to the
main class `io.vieira.adventuretime.Application`.

### Some precisions
- A little part of the project's code (such as class getters/setters/toString functions) is generated using the great
[Lombok plugin](https://projectlombok.org).