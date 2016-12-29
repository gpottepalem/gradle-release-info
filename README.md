Release-Info Gradle Plugin
==========================
[![Build Status](https://travis-ci.org/gpottepalem/gradle-release-info.svg?branch=master)](https://travis-ci.org/gpottepalem/gradle-release-info)

A simple __Gradle plugin__ which holds release-info details passed-in as gradle project properties through ```-P``` flag 
or as system properties through ```-D``` flag to the build script, 
and replaces passed-in release-info property names found in ```application.yml``` file with their respective values in 
the deployable archive(```war```/```jar```) created.
 * This plugin provides just one task ```releaseInfo``` and makes ```processResources``` task depend on 
 this task provided.
 * The release-info property names that need to be replaced with their values in <code>application.yml</code> **must** 
 be set as ANT like filter tokens. e.g. '```@myTokenToBereplaced@```'.

Installation
-----
Add maven repository and classpath dependency to the buildscript code block in your project's main Gradle build file as
shown below:

__```build.gradle```__

```groovy
buildscript {
    repositories {
        maven { url "https://dl.bintray.com/goodstartgenetics/gradle-plugins/" }
    }
    dependencies {
        classpath 'com.gsgenetics.plugins.gradle:release-info:1.0'
    }
}

apply plugin:"com.gsgenetics.plugins.gradle.release-info"

```

Usage
-----
Add your application specific release-info configuration variables with their value variable names in ANT like filter 
token format in the application's static configuration file: ```application.yml```

For example, if ```info.app.gitRevision``` and ```info.app.buildTag``` needs to be set with the actual git revision
and build tag values passed to ```gradle assemble```  task run by  passing values either through ```-P``` or ```-D``` 
arguments like,
 
 ```gradlew -DGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -DBUILD_TAG=GW-GWM-JOB1-30 assemble```
 
then specify those variables that needs to be resolved during assemble as shown below:

__```application.yml```__
```yaml
info:
    app:
        gitRevision: '@GIT_REVISION@'
        buildTag: '@BUILD_TAG@'
```

or

```yaml
info.app.gitRevision: '@GIT_REVISION@'
info.app.buildTag: '@BUILD_TAG@'
```

When you assemble your artifact, pass in release info arguments like:

```gradlew -DGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -DBUILD_TAG=GW-GWM-JOB1-30 assemble --info```

or

```gradlew -PGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -PBUILD_TAG=GW-GWM-JOB1-30 assemble --info```

The generated artifact (```war/jar```) will have ```'@GIT_REVISION@'``` and ```'@BUILD_TAG@'``` replaced with the actual values 
passed to ```assemble``` task.