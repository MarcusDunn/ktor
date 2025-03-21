/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.client.plugins.resources

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.resources.*
import io.ktor.resources.serialization.ResourcesFormat
import io.ktor.util.AttributeKey
import io.ktor.util.Attributes
import kotlinx.serialization.*
import io.ktor.client.request.delete as deleteBuilder
import io.ktor.client.request.get as getBuilder
import io.ktor.client.request.head as headBuilder
import io.ktor.client.request.options as optionsBuilder
import io.ktor.client.request.patch as patchBuilder
import io.ktor.client.request.post as postBuilder
import io.ktor.client.request.prepareDelete as prepareDeleteBuilder
import io.ktor.client.request.prepareGet as prepareGetBuilder
import io.ktor.client.request.prepareHead as prepareHeadBuilder
import io.ktor.client.request.prepareOptions as prepareOptionsBuilder
import io.ktor.client.request.preparePatch as preparePatchBuilder
import io.ktor.client.request.preparePost as preparePostBuilder
import io.ktor.client.request.preparePut as preparePutBuilder
import io.ktor.client.request.prepareRequest as prepareRequestBuilder
import io.ktor.client.request.put as putBuilder
import io.ktor.client.request.request as requestBuilder

/**
 * Executes a [HttpClient] GET request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.get)
 */
public suspend inline fun <reified T : Any> HttpClient.get(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val resources = resources()
    return getBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Executes a [HttpClient] POST request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.post)
 */
public suspend inline fun <reified T : Any> HttpClient.post(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val resources = resources()
    return postBuilder {
        attributes.attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Executes a [HttpClient] PUT request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.put)
 */
public suspend inline fun <reified T : Any> HttpClient.put(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val resources = resources()
    return putBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Executes a [HttpClient] DELETE request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.delete)
 */
public suspend inline fun <reified T : Any> HttpClient.delete(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val resources = resources()
    return deleteBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Executes a [HttpClient] PATCH request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.patch)
 */
public suspend inline fun <reified T : Any> HttpClient.patch(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val resources = resources()
    return patchBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Executes a [HttpClient] OPTIONS request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.options)
 */
public suspend inline fun <reified T : Any> HttpClient.options(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val resources = resources()
    return optionsBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Executes a [HttpClient] HEAD request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.head)
 */
public suspend inline fun <reified T : Any> HttpClient.head(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpResponse {
    val resources = resources()
    return headBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Executes a [HttpClient] request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.request)
 */
public suspend inline fun <reified T : Any> HttpClient.request(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit
): HttpResponse {
    val resources = resources()
    return requestBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] GET request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.prepareGet)
 */
public suspend inline fun <reified T : Any> HttpClient.prepareGet(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return prepareGetBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] POST request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.preparePost)
 */
public suspend inline fun <reified T : Any> HttpClient.preparePost(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return preparePostBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] PUT request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.preparePut)
 */
public suspend inline fun <reified T : Any> HttpClient.preparePut(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return preparePutBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] DELETE request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.prepareDelete)
 */
public suspend inline fun <reified T : Any> HttpClient.prepareDelete(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return prepareDeleteBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] PATCH request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.preparePatch)
 */
public suspend inline fun <reified T : Any> HttpClient.preparePatch(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return preparePatchBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] OPTIONS request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.prepareOptions)
 */
public suspend inline fun <reified T : Any> HttpClient.prepareOptions(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return prepareOptionsBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] HEAD request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.prepareHead)
 */
public suspend inline fun <reified T : Any> HttpClient.prepareHead(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return prepareHeadBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

/**
 * Prepares a [HttpClient] request, with a URL built from [resource] and the information from the [builder]
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.client.plugins.resources.prepareRequest)
 */
public suspend inline fun <reified T : Any> HttpClient.prepareRequest(
    resource: T,
    builder: HttpRequestBuilder.() -> Unit = {}
): HttpStatement {
    val resources = resources()
    return prepareRequestBuilder {
        attributes.putUrlTemplate(resources.resourcesFormat, serializer<T>())
        href(resources.resourcesFormat, resource, url)
        builder()
    }
}

public val URL_TEMPLATE: AttributeKey<String> = AttributeKey<String>("URL_TEMPLATE")

public fun <T : Any> Attributes.putUrlTemplate(
    format: ResourcesFormat,
    serializer: KSerializer<T>,
) {
    put(URL_TEMPLATE, urlTemplate(format, serializer))
}

internal fun <T> urlTemplate(
    format: ResourcesFormat,
    serializer: KSerializer<T>,
): String {
    val path = format.encodeToPathPattern(serializer)
    val query = format.encodeToQueryParameters(serializer)
    return buildString {
        append(path)
        for ((i, parameter) in query.withIndex()) {
            append(if (i == 0) '?' else '&')
            append(parameter.name)
            append("={")
            append(parameter.name)
            if (parameter.isOptional) {
                append('?')
            }
            append('}')
        }
    }
}

@PublishedApi
internal fun HttpClient.resources(): io.ktor.resources.Resources {
    return pluginOrNull(Resources) ?: throw IllegalStateException("Resources plugin is not installed")
}
