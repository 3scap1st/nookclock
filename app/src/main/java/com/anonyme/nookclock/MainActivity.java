package com.anonyme.nookclock;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

	private Handler handler = new Handler();
	private TextView timeTextView;
	private TextView dayTextView;
	private TextView dateTextView;
	private TextView batteryTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up full screen and landscape mode
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// Keep the screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Create a LinearLayout
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setBackgroundColor(Color.WHITE); // Set background color to white

		// Create a TextView for time
		timeTextView = new TextView(this);
		timeTextView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		timeTextView.setTextSize(getMaxTextSizeForTime());
		timeTextView.setTextColor(Color.BLACK); // Set text color to black
		timeTextView.setTypeface(null, Typeface.BOLD); // Set text to bold
		linearLayout.addView(timeTextView);

		// Create a TextView for day
		dayTextView = new TextView(this);
		LinearLayout.LayoutParams dayParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		dayParams.setMargins(0, -40, 0, 0);
		dayTextView.setLayoutParams(dayParams);
		dayTextView.setTextSize(getMaxTextSize());
		dayTextView.setTextColor(Color.BLACK); // Set text color to black
		linearLayout.addView(dayTextView);

		// Create a TextView for date
		dateTextView = new TextView(this);
		dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		dateTextView.setTextSize(getMaxTextSize());
		dateTextView.setTextColor(Color.BLACK); // Set text color to black
		linearLayout.addView(dateTextView);

		// Create a TextView for battery and temperature
		batteryTextView = new TextView(this);
		batteryTextView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		batteryTextView.setTextSize(getMaxTextSize());
		batteryTextView.setTextColor(Color.BLACK); // Set text color to black
		linearLayout.addView(batteryTextView);

		// Set the content view to the LinearLayout
		setContentView(linearLayout);

		// Start the clock update
		startClock();
	}

	private void startClock() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				updateClock();
				handler.postDelayed(this, 60000); // 1 minute interval
			}
		}, 1000);
	}

	private void updateClock() {
		// Get current time, day, and date
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());

		String currentTime = timeFormat.format(new Date());
		String currentDay = dayFormat.format(new Date());
		String currentDate = dateFormat.format(new Date());

		// Update TextViews with current time, day, and date
		timeTextView.setText(currentTime);
		dayTextView.setText(currentDay);
		dateTextView.setText(currentDate);

		// Update battery percentage and temperature
		updateBatteryInfo();
	}

	private void updateBatteryInfo() {
		// Update battery percentage
		updateBatteryPercentage();

		// Attempt to update temperature
		updateTemperature();
	}

	private void updateBatteryPercentage() {
		Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

		if (batteryIntent != null) {
			int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

			if (level != -1 && scale != -1) {
				int batteryPercentage = (int) ((level / (float) scale) * 100);
				batteryTextView.setText("Bat: " + batteryPercentage + "%");
			}
		}
	}

	private void updateTemperature() {
		try {
			Process process = Runtime.getRuntime().exec("cat /sys/devices/platform/omap3epfb.0/graphics/fb0/epd_temp");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String temperatureString = reader.readLine();

			if (temperatureString != null) {
				int temperature = Integer.parseInt(temperatureString);

				// Display temperature information
				batteryTextView.append(" Temp: " + temperature + " °C");
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private float getMaxTextSize() {
		// Get the display metrics to determine screen dimensions
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		// Calculate the maximum text size based on screen width (heightPixels because of landscape orientation)
		float screenWidth = displayMetrics.heightPixels;
		return screenWidth / 8; // Adjust this factor as needed
	}

	private float getMaxTextSizeForTime() {
		// Get the display metrics to determine screen dimensions
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		// Calculate the maximum text size based on screen width
		float screenWidth = displayMetrics.heightPixels;
		return screenWidth / 2; // Adjust this factor as needed
	}
}