/*
 * Copyright 2014-2026 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.server.plugins.ratelimit

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class RateLimitIntegrationTest {

    @Test
    fun rateLimitRunsBeforeMockAuthWhenRateLimitWrapsAuthenticate() = testApplication {
        install(RateLimit) {
            register(RateLimitName("block-all")) {
                rateLimiter(limit = 0, refillPeriod = 1.seconds)
            }
        }

        routing {
            rateLimit(RateLimitName("block-all")) {
                mockAuthenticate {
                    post("/endpoint") {
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }

        assertEquals(HttpStatusCode.TooManyRequests, client.post("/endpoint").status)
    }

    @Test
    fun mockAuthRunsBeforeRateLimitWhenAuthenticateWrapsRateLimit() = testApplication {
        install(RateLimit) {
            register(RateLimitName("block-all")) {
                rateLimiter(limit = 0, refillPeriod = 1.seconds)
            }
        }

        routing {
            mockAuthenticate {
                rateLimit(RateLimitName("block-all")) {
                    post("/endpoint") {
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }

        assertEquals(HttpStatusCode.Unauthorized, client.post("/endpoint").status)
        assertEquals(
            HttpStatusCode.TooManyRequests,
            client.post("/endpoint") { header(HttpHeaders.Authorization, "user1") }.status,
        )
    }

    @Test
    fun requestKeyHasAccessToPrincipalWhenAuthenticateWrapsRateLimit() = testApplication {
        install(RateLimit) {
            register(RateLimitName("per-user")) {
                rateLimiter(limit = 1, refillPeriod = 1000.seconds)
                requestKey { call -> call.attributes[MockPrincipalKey] }
            }
        }

        routing {
            mockAuthenticate {
                rateLimit(RateLimitName("per-user")) {
                    post("/endpoint") {
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }

        suspend fun post(user: String): HttpStatusCode =
            client.post("/endpoint") { header(HttpHeaders.Authorization, user) }.status

        assertEquals(HttpStatusCode.OK, post("user1"))
        assertEquals(HttpStatusCode.TooManyRequests, post("user1"))
        assertEquals(HttpStatusCode.OK, post("user2"))
    }
}

private val MockAuthenticatePhase = PipelinePhase("Authenticate")

private object MockAuthenticationHook : Hook<suspend (ApplicationCall) -> Unit> {
    override fun install(pipeline: ApplicationCallPipeline, handler: suspend (ApplicationCall) -> Unit) {
        pipeline.insertPhaseAfter(ApplicationCallPipeline.Plugins, MockAuthenticatePhase)
        pipeline.intercept(MockAuthenticatePhase) { handler(call) }
    }
}

private val MockPrincipalKey = AttributeKey<String>("MockPrincipal")

private val MockAuthenticationInterceptors = createRouteScopedPlugin("MockAuthenticationInterceptors") {
    on(MockAuthenticationHook) { call ->
        if (call.isHandled) return@on
        val credentials = call.request.headers[HttpHeaders.Authorization]
        if (credentials == null) {
            call.respond(HttpStatusCode.Unauthorized)
        } else {
            call.attributes.put(MockPrincipalKey, credentials)
        }
    }
}

private class MockAuthenticationRouteSelector : RouteSelector() {
    override suspend fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation =
        RouteSelectorEvaluation.Transparent
}

private fun Route.mockAuthenticate(build: Route.() -> Unit): Route {
    val authenticatedRoute = createChild(MockAuthenticationRouteSelector())
    authenticatedRoute.install(MockAuthenticationInterceptors)
    authenticatedRoute.build()
    return authenticatedRoute
}
