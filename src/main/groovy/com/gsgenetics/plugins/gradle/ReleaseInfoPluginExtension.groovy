package com.gsgenetics.plugins.gradle

/**
 * Plugin Extension object that holds all properties that get passed to project's build script via -P (project level) or
 * -D .
 * The properties to be held by this object are made dynamic with a Map of propertiesHolder and propertyMissing methods.
 *
 * @author Gpottepalem
 * Created on Dec 22, 2016
 *
 * @see http://groovy-lang.org/metaprogramming.html#_propertymissing
 * @see https://docs.gradle.org/current/userguide/custom_plugins.html#sec:getting_input_from_the_build
 */
class ReleaseInfoPluginExtension {
    /** default release info variables that can be passed to project's build script via -D or -P options */
    static final String[] RELEASE_INFO_VARIABLES = ['GIT_REVISION', 'BUILD_TAG']

    /** Dynamic propertiesHolder holder */
    Map propertiesHolder = [:]

    /**
     * Intercepts the propertyMissing call when a property resolution fails, adds the property and value to the holder.
     * Acts like a dynamic property setter.
     *
     * @param name the property name
     * @param value the property value
     * @return
     */
    def propertyMissing(String name, value) {
        propertiesHolder[name] = value
    }

    /**
     * Intercepts the propertyMissing call, teturns property value
     *
     * Acts like a dynamic property getter.
     *
     * @param name property name
     * @return property value if found, <code>null</code> otherwise
     */
    def propertyMissing(String name) {
        return propertiesHolder[name]
    }
}