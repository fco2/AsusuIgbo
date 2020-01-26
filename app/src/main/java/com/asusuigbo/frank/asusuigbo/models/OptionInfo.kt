package com.asusuigbo.frank.asusuigbo.models

import java.io.Serializable

class OptionInfo(var Option: String,
                      var Audio: String,
                      var AdditionalInfo: String = "NA") : Serializable{
    constructor(): this("", "", "")
}