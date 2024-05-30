package com.example.androidlab5

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private lateinit var pressureTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var weatherForecastTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pressureTextView = findViewById(R.id.pressureTextView)
        temperatureTextView = findViewById(R.id.temperatureTextView)
        humidityTextView = findViewById(R.id.humidityTextView)
        weatherForecastTextView = findViewById(R.id.weatherForecastTextView)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        if (pressureSensor != null) {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            pressureTextView.text = "Pressure sensor not available"
        }

        // Аналогічно можна додати інші сенсори, такі як датчик температури і вологості.
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            val pressure = event.values[0]
            pressureTextView.text = "Pressure: $pressure hPa"

            // Додати логіку для прогнозу погоди на основі змін тиску.
            val forecast = getWeatherForecast(pressure)
            weatherForecastTextView.text = "Weather Forecast: $forecast"
        }
        // Додати обробку даних для інших сенсорів.
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Нічого не робимо, але метод має бути реалізований.
    }

    private fun getWeatherForecast(pressure: Float): String {
        // Проста логіка для прогнозу погоди на основі тиску.
        return when {
            pressure > 1013 -> "Sunny"
            pressure < 1000 -> "Rainy"
            else -> "Cloudy"
        }
    }
}
