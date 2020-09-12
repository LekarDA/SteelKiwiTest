package com.example.steelkiwi

import java.lang.StringBuilder
import java.util.*

class CardInteractor {

    private val CARD_FIELD_SIZE = 4
    private val EXPIRE_DATE_SIZE = 5
    private val SECURITY_CODE_SIZE = 3

    private var messageCardError = ""
    private var messageExpire = ""
    private var messageSecurity = ""
    private var result = true

    private val cardError = "Error in field of card number"
    private val expireError = "Error in expire date field"
    private val securityError = "Error in security code field"

    private var cardNumber = ""
    private var expireDate = ""
    private var securityCode = ""


    fun isCardNumberValid(
        cardNumber1: String,
        cardNumber2: String,
        cardNumber3: String,
        cardNumber4: String
    ):Boolean {
        setInitState()
        if (cardNumber1.length < CARD_FIELD_SIZE ||
            cardNumber2.length < CARD_FIELD_SIZE ||
            cardNumber3.length < CARD_FIELD_SIZE ||
            cardNumber4.length < CARD_FIELD_SIZE
        ) {
            messageCardError = cardError
            result = false
        } else {
            result = true
            cardNumber = "$cardNumber1 $cardNumber2 $cardNumber3 $cardNumber4"
        }
        return result
    }

    fun isExpireValid(expireDate: String): Boolean {
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val currentYear = year % 2000
        val currentMonth = month + 1

        if (expireDate.length == EXPIRE_DATE_SIZE) {
            val list = expireDate.split(Utils.SEPARATOR)
            val inputMonth = list[0].toInt()
            val inputYear = list[1].toInt()
            val isInputMonthValid = inputMonth in 1..12
            val isInputYearValid = inputYear in currentYear..currentYear + 5

            if (isInputMonthValid.not() || isInputYearValid.not()) {
                messageExpire = expireError
                return false
            } else if (inputYear == currentYear && inputMonth < currentMonth) {
                messageExpire = expireError
                return false
            } else {
                this.expireDate = expireDate
                return true}
        } else {
            messageExpire = expireError
            return false
        }
    }

    fun isSecurityValid(securityCode: String) :Boolean{
        return if(securityCode.length < SECURITY_CODE_SIZE) {
            messageSecurity = securityError
            false
        } else {
            this.securityCode = securityCode
            true
        }
    }

    fun getErrorMessage(): String {
        val sb = StringBuilder()
        if (messageCardError.isNotEmpty())
            sb.append(messageCardError)
        if(messageCardError.isNotEmpty() && messageSecurity.isNotEmpty() || messageExpire.isNotEmpty() && messageCardError.isNotEmpty())
                sb.append("\n")
        if (messageExpire.isNotEmpty())
            sb.append(messageExpire)
        if(messageExpire.isNotEmpty() && messageSecurity.isNotEmpty() || messageCardError.isNotEmpty() && messageSecurity.isNotEmpty())
            sb.append("\n")
        if(messageSecurity.isNotEmpty())
            sb.append(messageSecurity)
        return sb.toString()
    }

    fun setInitState(){
        messageSecurity = ""
        messageCardError = ""
        messageExpire = ""
        result = true
    }

    fun getCardDataJson(nameOfCard: String):String {
        var name = nameOfCard
        if(name.isEmpty()) name = "\"\""
        val sb = StringBuilder()
        sb
            .append("{").append("\n")
            .append("\t\t name : $name").append("\n")
            .append("\t\t cardNumber: $cardNumber").append("\n")
            .append("\t\t expireDate : $expireDate").append("\n")
            .append("\t\t securityCode : $securityCode").append("\n")
            .append("}")
        return sb.toString()
    }
}