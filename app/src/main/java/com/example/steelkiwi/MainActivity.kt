package com.example.steelkiwi

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder


class MainActivity : AppCompatActivity(), View.OnKeyListener, CustomTextWatcher {

    private val MAX_SIZE = 4
    private val MIN_SIZE = 0
    private var isDelClicked = false
    private val EXPIRE_CHAR_SIZE_FOR_ADD_SPLIT = 2
    private val EXPIRE_CHAR_SIZE_FOR_DEL_SPLIT = 3

    private lateinit var cardViewModel: CardViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardViewModel = ViewModelProvider(this).get(CardViewModel::class.java)

        et_cardNumber1.setOnKeyListener(this)
        et_cardNumber2.setOnKeyListener(this)
        et_cardNumber3.setOnKeyListener(this)
        et_cardNumber4.setOnKeyListener(this)
        et_expireDate.setOnKeyListener(this)
        et_expireDate.addTextChangedListener(this)

        btn_addCard.setOnClickListener { v ->
            cardViewModel.checkParams(
                et_cardName.text.toString(),
                et_cardNumber1.text.toString(),
                et_cardNumber2.text.toString(),
                et_cardNumber3.text.toString(),
                et_cardNumber4.text.toString(),
                et_expireDate.text.toString(),
                et_securityCode.text.toString()
                )
        }

        cardViewModel.isSuccess.observe(this, Observer<Boolean> {
            if(it) showDialog(cardViewModel.getCardData())
        })

        cardViewModel.errorMessage.observe(this,Observer<String>{
            if(it.isNotEmpty())
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun showDialog(cardJson: String) {
        val dialog = AlertDialog.Builder(this)
            .setMessage(cardJson)
            .setPositiveButton(resources.getString(R.string.btn_ok)) { dialog, id -> dialog.cancel() }
            .create()
        dialog.show()
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        when(view?.id){
            et_cardNumber1.id ->{
                val size = et_cardNumber1.length()
                if (size == MAX_SIZE && keyCode != KeyEvent.KEYCODE_DEL) et_cardNumber2.requestFocus()
            }
            et_cardNumber2.id ->{
                val size = et_cardNumber2.length()
                if (size == MAX_SIZE && keyCode != KeyEvent.KEYCODE_DEL) et_cardNumber3.requestFocus()
                else if(size == MIN_SIZE && keyCode == KeyEvent.KEYCODE_DEL) et_cardNumber1.requestFocus()
            }
            et_cardNumber3.id ->{
                val size = et_cardNumber3.length()
                if (size == MAX_SIZE && keyCode != KeyEvent.KEYCODE_DEL) et_cardNumber4.requestFocus()
                else if(size == MIN_SIZE && keyCode == KeyEvent.KEYCODE_DEL) et_cardNumber2.requestFocus()
            }
            et_cardNumber4.id ->{
                val size = et_cardNumber4.length()
                if(size == MIN_SIZE && keyCode == KeyEvent.KEYCODE_DEL) et_cardNumber3.requestFocus()
            }
            et_expireDate.id ->{
                isDelClicked = keyCode == KeyEvent.KEYCODE_DEL
            }
        }
        return false
    }

    override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if(inputText?.length == EXPIRE_CHAR_SIZE_FOR_ADD_SPLIT && isDelClicked.not()) {
            val sb =  StringBuilder()
            sb.append(inputText)
            sb.append(Utils.SEPARATOR)
            et_expireDate.setText(sb.toString())
            et_expireDate.setSelection(et_expireDate.text.length)
        }else if(inputText?.length == EXPIRE_CHAR_SIZE_FOR_DEL_SPLIT && isDelClicked){
            et_expireDate.setText(inputText.subSequence(0, inputText.length - 1))
            et_expireDate.setSelection(et_expireDate.text.length)
        }
    }
}