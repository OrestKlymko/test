package org.innovatrics.storage.user.model

import jakarta.persistence.*
import java.util.UUID


@Entity
@Table(name = "USERS")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    val id: UUID?=null,
    @Column(name = "USERNAME", nullable = false)
    val username: String,
    @Column(name = "PASSWORD", nullable = false)
    val password: String
)
