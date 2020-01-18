package com.example.moviecatalog.ui.reminderSetting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.moviecatalog.R

class ReminderSettingFragment : PreferenceFragmentCompat() {

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        alarmReceiver = AlarmReceiver()
        preferenceScreen.findPreference<SwitchPreference>("isDailyOn")
            ?.setOnPreferenceChangeListener { preference, newValue ->
                if (newValue == true) {
                    val time = "07:00"
                    alarmReceiver.setRepeatingAlarm(
                        preference.context,
                        AlarmReceiver.TYPE_DAILY,
                        time
                    )
                } else {
                    alarmReceiver.cancelAlarm(preference.context, AlarmReceiver.TYPE_DAILY)
                }
                return@setOnPreferenceChangeListener true
            }
        preferenceScreen.findPreference<SwitchPreference>("isDailyReleaseOn")
            ?.setOnPreferenceChangeListener { preference, newValue ->
                if (newValue == true){
                    val time = "08:00"
                    alarmReceiver.setRepeatingAlarm(
                        preference.context,
                        AlarmReceiver.TYPE_RELEASE,
                        time
                    )
                } else {
                    alarmReceiver.cancelAlarm(preference.context, AlarmReceiver.TYPE_RELEASE)
                }
                return@setOnPreferenceChangeListener true
            }
    }
}