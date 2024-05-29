package com.example.androidlab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ResultFragment : Fragment() {

    companion object {
        private const val INPUT_KEY = "inputKey"

        fun newInstance(input: String): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString(INPUT_KEY, input)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        val textViewResult = view.findViewById<TextView>(R.id.textViewResult)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)

        // Прямо встановлюємо текст, виходячи з припущення, що він завжди валідний
        textViewResult.text = "Ви обрали тип товару: ${arguments?.getString(INPUT_KEY)}"

        buttonCancel.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return view
    }
}

