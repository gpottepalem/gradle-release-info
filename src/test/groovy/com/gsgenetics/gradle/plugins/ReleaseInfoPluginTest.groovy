package com.gsgenetics.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * Testcase to test plugin
 *
 * @author Giri Pottepalem
 * Created on Dec 22, 2016
 *
 * @see build.gradle for systemProperties passed for testing
 */
class ReleaseInfoPluginTest extends GroovyTestCase {

    static final String TEST_GIT_REVISION = 'TestGitRevision'
    static final String TEST_BUILD_TAG = 'TestBuildTag'

    @Override
    protected void setUp() throws Exception {
        super.setUp()
    }

    void testReleaseInfoTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.gsgenetics.plugins.gradle.release-info' //plugin id

        assertTrue project.tasks.releaseInfo != null

        println project.releaseInfo.gitRevision
        println project.releaseInfo.buildTag

        assertTrue project.releaseInfo.gitRevision == TEST_GIT_REVISION
        assertTrue project.releaseInfo.buildTag == TEST_BUILD_TAG
    }

}