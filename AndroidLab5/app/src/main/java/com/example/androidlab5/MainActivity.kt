package com.example.androidlab5

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private var temperatureSensor: Sensor? = null
    private var humiditySensor: Sensor? = null
    private lateinit var pressureTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var weatherForecastTextView: TextView
    private lateinit var pressureChart: LineChart
    private lateinit var temperatureChart: LineChart
    private lateinit var humidityChart: LineChart

    private val pressureEntries = ArrayList<Entry>()
    private val temperatureEntries = ArrayList<Entry>()
    private val humidityEntries = ArrayList<Entry>()
    private var timeIndex = 0f
    private var currentPressure: Float? = null
    private var currentTemperature: Float? = null
    private var currentHumidity: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherForecastTextView = findViewById(R.id.weatherForecastTextView)
        pressureTextView = findViewById(R.id.pressureTextView)
        temperatureTextView = findViewById(R.id.temperatureTextView)
        humidityTextView = findViewById(R.id.humidityTextView)
        pressureChart = findViewById(R.id.pressureChart)
        temperatureChart = findViewById(R.id.temperatureChart)
        humidityChart = findViewById(R.id.humidityChart)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        pressureSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        temperatureSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        humiditySensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        setupChart(pressureChart, "Pressure (hPa)", 0f, 1100f)
        setupChart(temperatureChart, "Temperature (°C)", -273.1f, 100f)
        setupChart(humidityChart, "Humidity (%)", 0f, 100f)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_PRESSURE -> {
                val pressure = event.values[0]
                currentPressure = pressure
                pressureTextView.text = "Pressure: $pressure hPa"
                pressureEntries.add(Entry(timeIndex, pressure))
                updateChart(pressureChart, pressureEntries)
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                val temperature = event.values[0]
                currentTemperature = temperature
                temperatureTextView.text = "Temperature: $temperature °C"
                temperatureEntries.add(Entry(timeIndex, temperature))
                updateChart(temperatureChart, temperatureEntries)
            }
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                val humidity = event.values[0]
                currentHumidity = humidity
                humidityTextView.text = "Humidity: $humidity %"
                humidityEntries.add(Entry(timeIndex, humidity))
                updateChart(humidityChart, humidityEntries)
            }
        }

        // Оновлюємо графіки, навіть якщо значення не змінюються
        if (currentTemperature != null) {
            temperatureEntries.add(Entry(timeIndex, currentTemperature!!))
            updateChart(temperatureChart, temperatureEntries)
        }

        if (currentHumidity != null) {
            humidityEntries.add(Entry(timeIndex, currentHumidity!!))
            updateChart(humidityChart, humidityEntries)
        }

        if (currentPressure != null && currentTemperature != null && currentHumidity != null) {
            val forecast = getWeatherForecast(currentPressure!!, currentTemperature!!, currentHumidity!!)
            weatherForecastTextView.text = "Weather Forecast: $forecast"
        }

        timeIndex += 1f
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Нічого не робимо, але метод має бути реалізований.
    }

    private fun getWeatherForecast(pressure: Float, temperature: Float, humidity: Float): String {
        return when {
            temperature > 30 -> "Hot"
            temperature < 0 && humidity > 80 -> "Snowy"
            temperature < 0 -> "Cold"
            humidity > 90 && pressure > 1010 -> "Foggy"
            pressure > 1020 && temperature > 25 && humidity < 50 -> "Very Sunny"
            pressure > 1013 && temperature > 20 && humidity < 60 -> "Sunny"
            pressure >= 1000 && temperature > 15 && humidity < 70 -> "Cloudy"
            pressure >= 980 && temperature < 20 && humidity > 70 -> "Rainy"
            else -> "Stormy"
        }
    }

    private fun setupChart(chart: LineChart, label: String, minY: Float, maxY: Float) {
        chart.axisRight.isEnabled = false
        chart.xAxis.isEnabled = false
        chart.description.isEnabled = false
        val yAxis: YAxis = chart.axisLeft
        yAxis.axisMinimum = minY
        yAxis.axisMaximum = maxY
        chart.data = LineData(LineDataSet(listOf(Entry(0f, 0f)), label).apply { setDrawValues(false) }) // Прибираємо числові значення
    }

    private fun updateChart(chart: LineChart, entries: ArrayList<Entry>) {
        val dataSet = LineDataSet(entries, "Data")
        dataSet.setDrawValues(false)
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }
}
