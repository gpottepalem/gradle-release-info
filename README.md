Release-Info Gradle Plugin
==========================
A simple __Gradle plugin__ which holds release-info details passed-in as gradle project properties through ```-P``` flag 
or as system properties through ```-D``` flag to the build script, 
and replaces passed-in release-info property names found in ```application.yml``` file with their respective values in 
the deployable archive(```war```/```jar```) created.
 * This plugin provides just one task ```releaseInfo``` and makes ```processResources``` task depend on 
 this task provided.
 * The release-info property names that need to be replaced with their values in <code>application.yml</code> **must** 
 be set as ANT like filter tokens. e.g. '```@myTokenToBereplaced@```'.

Usage
-----
__```build.gradle```__

```yaml
buildscript {
    repositories {
        //TODO
    }
    dependencies {
        classpath 'com.gsgenetics.plugins.gradle:release-info:1.0'
    }
}

apply plugin:"com.gsgenetics.plugins.gradle.release-info"

```

__```application.yml```__
```yaml
info:
    app:
        gitRevision: '@GIT_REVISION@'
        buildTag: '@BUILD_TAG@'
```

Build
-----
When you assemble your artifact, pass in release info arguments like:

```gradlew -DGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -DBUILD_TAG=GW-GWM-JOB1-30 assemble --info```

or

```gradlew -PGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -PBUILD_TAG=GW-GWM-JOB1-30 assemble --info```