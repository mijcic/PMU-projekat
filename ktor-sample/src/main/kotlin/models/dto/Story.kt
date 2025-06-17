package com.example.models.dto

import kotlinx.serialization.Serializable

/**
 * Auxiliary text content used as helper or context in the application.
 *
 * @property story The helper text string.
 */
@Serializable
data class Story(
    val story:String
)