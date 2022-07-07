package com.example.authfirebase.listener

import com.example.authfirebase.ui.model.DrinkModel

interface IDrinkLoadListener {
    fun onDrinkLoadSuccess(drinkModelList: List<DrinkModel>)
    fun onDrinkLoadFailed(message:String?)
}