package com.kkw.pawket.pet.domain

enum class PetType(val stringValue: String) {
    DOG("dog"),
    CAT("cat"),
    RABBIT("rabbit"),
    HAMSTER("hamster"),
    GUINEA_PIG("guinea_pig"),
    BIRD("bird"),
    MEERKAT("meerkat"),
    RACC00N("raccoon"),
    HEDGEHOG("hedghog"),

    ETC("etc"),
    ;

    companion object {
        fun fromString(value: String): PetType? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
