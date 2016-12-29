package com.gsgenetics.plugins.gradle

import groovy.util.logging.Slf4j
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.language.jvm.tasks.ProcessResources

/**
 * A simple gradle plugin which holds release details passed via -P or -D arguments to build script, replaces passed in
 * argument variables found in <code>application.yml</code> file with their values passed.
 * The variables in <code>application.yml</code> MUST be ANT like filter token to be replaced.
 * e.g. application.yml
 * ...
 * info:
 *     app:
 *         gitRevision: '@GIT_REVISION@'
 *         buildTag: '@BUILD_TAG@'
 * ...
 *
 * When the build script is run as shown below, it replaces '@GIT_REVISION@' and '@BUILD_TAG@' with
 * 9f53be067c40191fa018d598105c3568924b3ee5 and GW-GWM-JOB1-30 respectively in the application.yml before it assembles
 * and creates the deployeble archive file (war/jar):
 *
 * ./gradlew -DGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -DBUILD_TAG=GW-GWM-JOB1-30 assemble
 * or
 * ./gradlew -PGIT_REVISION=9f53be067c40191fa018d598105c3568924b3ee5 -PBUILD_TAG=GW-GWM-JOB1-30 assemble
 *
 * @author Giri Pottepalem
 * Created on Dec 22, 2016
 *
 * @see <a href="https://docs.gradle.org/current/userguide/custom_plugins.html">Gradle - Writing Custom Plugins</a>
 * @see <a href="https://docs.gradle.org/current/javadoc/org/gradle/language/jvm/tasks/ProcessResources.html">Gradle API - Class ProcessResources</a>
 * @see <a href="https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/AbstractCopyTask.html#filter(java.util.Map, java.lang.Class)">Gradle API - filter method</a>
 *
 * @since 1.0
 */
@Slf4j
class ReleaseInfoPlugin implements Plugin<Project> {
    /**
     * Utility method, converts a given string to camelCase by removing underscores.
     * @param text string to be converted
     * @param capitalize
     * @return camelCase string
     */
    static String toCamelCase(String text, boolean capitalize = false) {
        text = text.toLowerCase().replaceAll("(_)([A-Za-z0-9])",
            { Object[] it-> it[2].toUpperCase()})
        return capitalize ? text.capitalize() : text
    }

    @Override
    void apply(Project project) {
        project.extensions.create('releaseInfo', ReleaseInfoPluginExtension)
        project.task('releaseInfo') {
            ReleaseInfoPluginExtension.RELEASE_INFO_VARIABLES.each {
                String projectReleaseInfoProperty = toCamelCase(it)
                //Support both -P and -D
                project.releaseInfo."$projectReleaseInfoProperty" =
                    project.hasProperty(it) ? project.property(it) : (System.getProperty(it) ?: 'unknown')
                log.info "setting project.releaseInfo property $projectReleaseInfoProperty: " +
                    project.releaseInfo."$projectReleaseInfoProperty" + '...'
            }
        }

        ProcessResources processResourcesTask = project.tasks.find{ it instanceof ProcessResources }

        if (processResourcesTask) {
            log.info "found processResourcesTask: ${processResourcesTask.description}..."
            processResourcesTask.dependsOn 'releaseInfo'

            project.processResources {
                log.info "Replacing ${ReleaseInfoPluginExtension.RELEASE_INFO_VARIABLES} in application.yml with ${project.releaseInfo.propertiesHolder.values()}..."
                filesMatching("**/application.yml") {
                    //https://docs.gradle.org/current/javadoc/org/gradle/api/file/CopySpec.html#expand(java.util.Map)
                    //expand( [ 'BUILD_TAG'   : project.releaseInfo.gitRevision, 'GIT_REVISION': project.releaseInfo.buildTag])
                    filter(
                        ReplaceTokens,
                        tokens: ReleaseInfoPluginExtension.RELEASE_INFO_VARIABLES.collectEntries {
                            [(it): project.releaseInfo."${toCamelCase(it)}"]
                        }
                    )
                }
            }
        }
        else {
            log.warn("ProcessResources task not found in the project and hence not processing any resources...")
        }
    }
}