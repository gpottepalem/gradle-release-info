Release-Info Gradle Plugin
==========================
A simple __Gradle plugin__ which holds release details passed via ```-P``` or ```-D``` arguments to build script, 
replaces passed in argument variables found in ```application.yml``` file with their values in the deployable 
archive(```war```/```jar```) created.
 * This plugin provides just one task ```releaseInfo``` and makes ```processResources``` task depend on 
 this task provided.
 * The release info variables that need to be replaced in <code>application.yml</code> **must** be ANT like filter 
 tokens. e.g. '```@myTokenToBereplaced@```'.
 
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

```gradlew -DGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -DBUILD_TAG=GW-GWM-JOB1-30 assemble```

or

```gradlew -PGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -PBUILD_TAG=GW-GWM-JOB1-30 assemble```