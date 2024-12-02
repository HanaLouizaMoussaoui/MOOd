package com.example.mood.model.enums

enum class MoodTypeEnum(val id: Int, val mood: String) {
    HAPPY(1, "Happy"),
    SAD(2,"Sad"),
    ANGRY(3,"Angry"),
    ANXIOUS(4,"Anxious"),
    EXCITED(5,"Excited"),
    CALM(6,"Calm"),
    CONFUSED(7,"Confused"),
    NEUTRAL(8,"Neutral")
}