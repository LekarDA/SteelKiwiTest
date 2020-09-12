package com.example.steelkiwi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel: ViewModel() {

    var isSuccess = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    val cardInteractor = CardInteractor()
    lateinit var nameOfCard: String

    fun checkParams(
        name: String,
        cardNumber1: String,
        cardNumber2: String,
        cardNumber3: String,
        cardNumber4: String,
        expireDate: String,
        securityCode: String
    ){
        nameOfCard = name
        val isCardNumberValid = cardInteractor.isCardNumberValid(
            cardNumber1,
            cardNumber2,
            cardNumber3,
            cardNumber4
        )

        val isSecurityCodeValid = cardInteractor.isSecurityValid(securityCode)

        val isExpireDateValid = cardInteractor.isExpireValid(expireDate)
        isSuccess.value = isCardNumberValid && isExpireDateValid && isSecurityCodeValid
        errorMessage.value = if(isSuccess.value?.not()!!) cardInteractor.getErrorMessage() else ""
    }

    fun getCardData() = cardInteractor.getCardDataJson(nameOfCard)
}