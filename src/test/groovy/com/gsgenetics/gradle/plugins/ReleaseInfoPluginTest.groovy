package com.gsgenetics.gradle.plugins

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * Testcase to test plugin
 *
 * @see build.gradle for
 * @author Gpottepalem
 * Created on Dec 22, 2016
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
        project.pluginManager.apply 'releaseInfo'

        assertTrue project.tasks.releaseInfo != null

        println project.releaseInfo.gitRevision
        println project.releaseInfo.buildTag

        assertTrue project.releaseInfo.gitRevision == TEST_GIT_REVISION
        assertTrue project.releaseInfo.buildTag == TEST_BUILD_TAG
    }

}