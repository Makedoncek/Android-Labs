package com.example.androidlab1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), InputFragment.OnInputSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Відображення InputFragment як початкового фрагмента
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, InputFragment())
                .commit()
        }
    }

    // Реалізація інтерфейсу OnInputSelectedListener з використанням newInstance
    override fun onInputSelected(input: String) {
        // Створення нового екземпляру ResultFragment за допомогою методу newInstance
        val resultFragment = ResultFragment.newInstance(input)

        // Заміна InputFragment на ResultFragment, додавання транзакції до стеку викликів
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, resultFragment)
            .addToBackStack(null) // Дозволяє користувачу повернутися до InputFragment
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) // Додає анімацію при переході
            .commit()
    }
}
