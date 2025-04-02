package com.kkw.pawket.checklist.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity(name = "check_type")
data class CheckType(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = true)
    val description: String? = null

) : BaseEntity() {
    constructor(
        name: String,
        description: String? = null,
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        description = description,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CheckType
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        private val regex = Regex("^[A-Za-z]+$")

        fun create(name: String, description: String? = null): CheckType {
            require(regex.matches(name)) {
                "CheckType name must contain only English letters (A-Z, a-z)."
            }

            val formattedName = name.lowercase().replaceFirstChar { it.uppercaseChar() }

            return CheckType(
                name = formattedName,
                description = description
            )
        }
    }
}
