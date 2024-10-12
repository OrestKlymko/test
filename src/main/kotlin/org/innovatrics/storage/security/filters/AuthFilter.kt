package org.innovatrics.storage.security.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.websocket.AuthenticationException
import org.innovatrics.storage.files.service.type.AttachmentService
import org.innovatrics.storage.security.service.JwtService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.slf4j.Logger

@Component
class AuthFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(AttachmentService::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val token = extractToken(request)
        if (token != null) {
            try {
                authenticate(token)
            } catch (e: Exception) {
                log.warn("Authentication failed: ${e.message}")
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    private fun authenticate(token: String) {
        val parsedClaims = jwtService.parseJwtToken(token)
        parsedClaims?.let {
            val authenticationToken = UsernamePasswordAuthenticationToken(
                it.subject, null, emptyList()
            )
            SecurityContextHolder.getContext().authentication = authenticationToken
        } ?: throw AuthenticationException("Invalid JWT token")
    }
}
