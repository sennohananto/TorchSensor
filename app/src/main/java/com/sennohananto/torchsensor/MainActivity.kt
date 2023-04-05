package com.sennohananto.torchsensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var cameraManager: CameraManager
    private lateinit var torchButton: Button
    private var lightLevel: Float = 0.0f
    private var torchOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        torchButton = findViewById(R.id.torch_button)

        // Register the light sensor
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // Set the torch button click listener
        torchButton.setOnClickListener {
            toggleTorch()
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type == Sensor.TYPE_LIGHT) {
            lightLevel = p0.values[0]
            if (lightLevel < 10.0f && !torchOn) {
                toggleTorch()
            } else {
                toggleTorch()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun toggleTorch() {
        try {
            val cameraId = cameraManager.cameraIdList[0]
            if (torchOn) {
                cameraManager.setTorchMode(cameraId, false)
                torchOn = false
                torchButton.text = "Turn on torch"
            } else {
                cameraManager.setTorchMode(cameraId, true)
                torchOn = true
                torchButton.text = "Turn off torch"
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}