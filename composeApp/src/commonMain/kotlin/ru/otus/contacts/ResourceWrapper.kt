package ru.otus.contacts

import org.jetbrains.compose.resources.StringResource

/**
 * Provides resources
 */
interface ResourceWrapper {
    /**
     * Returns a resource string
     */
    suspend fun getString(resource: StringResource, vararg args: Any): String
}

object ComposeResourceWrapper : ResourceWrapper {
    /**
     * Returns a resource string
     */
    override suspend fun getString(resource: StringResource, vararg args: Any): String {
        return org.jetbrains.compose.resources.getString(resource, *args)
    }
}