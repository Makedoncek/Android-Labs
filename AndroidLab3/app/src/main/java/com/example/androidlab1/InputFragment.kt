package com.example.androidlab1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class InputFragment : Fragment() {
    // Інтерфейс для комунікації з активністю
    interface OnInputSelectedListener {
        fun onInputSelected(input: String)
    }

    private var listener: OnInputSelectedListener? = null

    // Прикріплення фрагмента до активності
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnInputSelectedListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnInputSelectedListener")
        }
    }

    // Створення представлення для фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input, container, false)
        val radioGroupType = view.findViewById<RadioGroup>(R.id.radioGroupType)
        val radioGroupBrand = view.findViewById<RadioGroup>(R.id.radioGroupBrand)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)

        buttonOk.setOnClickListener {
            val selectedTypeId = radioGroupType.checkedRadioButtonId
            val selectedBrandId = radioGroupBrand.checkedRadioButtonId

            if (selectedTypeId == -1 || selectedBrandId == -1) {
                // Якщо одна з груп радіокнопок не має вибраного значення, показуємо Toast
                Toast.makeText(context,getString(R.string.prompt_select_goods_and_brand), Toast.LENGTH_LONG).show()
            } else {
                // Якщо обидва параметри вибрані, можна продовжувати з обробкою даних
                val selectedType = view.findViewById<RadioButton>(selectedTypeId).text.toString()
                val selectedBrand = view.findViewById<RadioButton>(selectedBrandId).text.toString()
                listener?.onInputSelected("$selectedType, $selectedBrand")
            }
        }

        return view
    }

}
