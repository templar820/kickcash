package com.example.kickcash



class Singleton {
    private var instance: Singleton? = null
    private var value: String? = null

    companion object {
        val instance = Singleton()
    }
    fun setValue(el: String){
        println("Присвоено!!!")
        value = el
    }

    fun getValue() : String?{
        return value
    }
}