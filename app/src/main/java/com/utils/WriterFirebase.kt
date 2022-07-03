package com.utils

class WriterFirebase {
    var fullName: String? = null
    var yearBirth: Int? = null
    var yearDied: Int? = null
    var typeOfLiterature: Int? = null
    var fullInformation: String? = null
    var image: String? = null
    var favourite: Int? = null

    constructor()
    constructor(
        fullName: String?,
        yearBirth: Int?,
        yearDied: Int?,
        typeOfLiterature: Int?,
        fullInformation: String?,
        image: String?,
        favourite: Int?,
    ) {
        this.fullName = fullName
        this.yearBirth = yearBirth
        this.yearDied = yearDied
        this.typeOfLiterature = typeOfLiterature
        this.fullInformation = fullInformation
        this.image = image
        this.favourite = favourite
    }


}