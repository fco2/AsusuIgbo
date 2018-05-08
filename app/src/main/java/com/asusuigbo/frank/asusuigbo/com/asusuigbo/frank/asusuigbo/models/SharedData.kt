package com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models

class SharedData {
    companion object {
        var SelectedAnswerIndex: Int = -1
        var CorrectAnswerIndex: Int = -1
        var CurrentListIndex: Int = 0
        var ButtonState: String = "Check Answer" // Check Answer, NextQuestion, Finished
    }
}