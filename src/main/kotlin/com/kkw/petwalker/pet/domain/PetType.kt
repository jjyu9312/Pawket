package com.kkw.petwalker.pet.domain

enum class PetType(val stringValue: String) {
    DOG("강아지"),
    CAT("고양이"),
    RABBIT("토끼"),
    HAMSTER("햄스터"),
    GUINEA_PIG("기니피그"),
    BIRD("새"),
    MEERKAT("미어캣"),
    RACC00N("너구리"),
    HEDGEHOG("고슴도치"),
    ETC("기타"),
    ;

    companion object {
        fun fromString(value: String): PetType? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
