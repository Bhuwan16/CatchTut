package com.example.catchtut

class TeacherData {
    var name = ""
    var surname = ""
    var state = ""
    var city = ""
    var phone = ""
    var area = ""
    var mode = ""
    var uri = ""
    var uid = ""
    var classMap = HashMap<String,String>()
    var subjectMap = HashMap<String,String>()
}

class TeachData{
    var isInfoSaved = false
    var name = ""
    var surname = ""
    var stars = ""
    var state = ""
    var city = ""
    var phone = ""
    var area = ""
    var uid = ""
    var mode = ""
    var classMap = HashMap<String,String>()
    var subjectMap = HashMap<String,String>()
    var uri = ""

}

class FilterData{
    var mode = ""
    var classes = ""
    var subject = ""
    var city = ""
}

class Review{
    var name = ""
    var review = ""
}