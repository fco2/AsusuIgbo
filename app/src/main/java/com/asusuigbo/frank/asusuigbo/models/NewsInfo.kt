package com.asusuigbo.frank.asusuigbo.models

class NewsInfo(
    var title: String,
    var newsSource: String,
    var url: String,
    var imageUrl: String,
    var content: String,
    var audioUrl: String,
    var date: String   //this will tell if news is a big heading or small heading
){
    constructor(): this("","","","", "","", "")
}