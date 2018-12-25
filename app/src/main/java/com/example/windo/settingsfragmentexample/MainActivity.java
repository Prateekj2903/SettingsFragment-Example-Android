package com.example.windo.settingsfragmentexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/*

1. Make an empty settings activity
2. Create a menu item which take the main activity to settings activity
3. (a) Get the actionBar object by this.getSupportActionBar and use the object to setDisplayHomeAsEnabled(true)
    if the object variable is not null
   (b) Override the onOptionsItemSelected and get the id. If id is android.R.id.home do
    NavUtils.navigateUpFromSameTask(this);
4. (a) In the manifest file set the launchMode as single top for the main activity
   (b) Make the parent of SettingsActivity as MainActivity

5. Add gradle dependency for support preference fragment
   compile 'com.android.support:preference-v7:25.1.0'
6. Create a class called SettingsFragment that extends PreferenceFragmentCompat
7. Create a res dir called xml and create a .xml file called pref_visualizer
8. (a) Add the preferences like CheckedBoxPreference, ListPreference, EditTextPreference etc.
   (b) Give these preferences ids, title and defaultValue. (Use bools.xml file to create boolean default values
   (c) In case of ListPreference add array and array values for list items by adding an arrays resource
       in values dir. - Use <array name = "" > and add items for each item
   (d) Use showSummary and hideSummary attributes to show and hide summary for CheckedBoxPreference
   (e) For List and Edit Text Preference summary will be added in later steps.

9. Override the onCreatePreferences method in SettingsFragment and use addPreferencesFromResource(R.xml.pref.visualiser)
10. Set the root layout of the settings activity layout to fragment
11. Specify a theme in styles xml file
    <item name="preferenceTheme">@style/PreferenceThemeOverlay</item>

12. To read from SharedPreferences
    (a) Get a reference to the default shared preferences from the PreferenceManager class
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    (b) Get the values by using the getString, getBoolean etc functions. and pass the key and default value as params.

13. To make the changes apply use OnSharedPreferenceChangeListener
    (a) Implement SharedPreferences.OnSharedPreferenceChangeListener from MainActivity
    (b) Override the onSharedPreferenceChanged method and update the show bass preference
    (c) Check id the key is equal to the current preference key then update the value
    (d) Register the listener in OnCreate method using
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    (e) Unregister the listener in OnDestroy method using
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

14. To show summary of list preference and edit text preference
    (a) Implement SharedPreferences.OnSharedPreferenceChangeListener from SettingsFragment
    (b) create a method setPreferenceSummary that takes a Preference and String value as parameter
        (b.1) Check if preference is instance of ListPreference or EditTextPreference
            (b.1.1) If ListPreference : Cast the preference to List Preference
                    Find prefIndex by value using the preference.findIndexByValue(value) method
                    If this index >= 0 ie if valid index set summary as
                    preference.setSummary(preference.getEntries()[prefIndex])
            (b.1.2) If EditTextPreference : set summary by
                    preference.setSummary(value)

     (c) In OnCreatePreference method
        (c.1) Make object of SharedPreferenceScreen by : SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        (c.2) Get the preference screen and preference count by
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            int count = preferenceScreen.getPreferenceCount();
        (c.3) Iterate through all of the preferences and get current preference by : Preference p = preferenceScreen.getPreference(i);
             if it is not a checkbox preference, call the setSummary method passing in a preference and
            the value of the preference : String value = sharedPreferences.getString(p.getKey(), "");

     (d) Override onSharedPreferenceChanged and, if it is not a checkbox preference,
        call setPreferenceSummary on the changed preference
        (d.1) Find preference by key : Preference preference = findPreference(key);
        (d.2) If preference is not null check if it not instanceOf CheckBoxPreference
        (d.3) If not call the setPreferenceSummary method.

     (e) Register and unregister the listener.

15. To add constraints before saving to SharedPreference file
    (a) Implement Preference.OnPreferenceChangeListener from SettingsFragment
    (b) Override onPreferenceChange method
        (b.1) Check if the key matches the preference.geyKey() of required preference type
        (b.2) Cast the newValue parameter to required type
        (b.3) if the conditions are not met show toast and return false else return true
    (c) In onCreatePreference method add the OnPreferenceChangeListener specifically to the required preference type
        (c.1) First find the preference by its key :
            Preference preference = findPreference(getString(R.string.pref_size_key));
        (C.2) preference.setOnPreferenceChangeListener(this);
 */


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    TextView mCheckBoxTextView;
    TextView mListPrefTextView;
    TextView mEditPrefTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCheckBoxTextView = (TextView) findViewById(R.id.tv_check_box);
        mListPrefTextView = (TextView) findViewById(R.id.tv_list_pref);
        mEditPrefTextView = (TextView) findViewById(R.id.tv_edit_pref);

        setupSharedPreferences();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void setupSharedPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setCheckBoxValue(sharedPreferences);
        setListPrefValue(sharedPreferences);
        setEditPrefValue(sharedPreferences);
    }

    private void setCheckBoxValue(SharedPreferences sharedPreferences){
        Boolean defaultCheckBoxValue = sharedPreferences.getBoolean(getString(R.string.pref_check_key),
                getResources().getBoolean(R.bool.pref_check_default));
        mCheckBoxTextView.setText(defaultCheckBoxValue.toString());
    }

    private void setListPrefValue(SharedPreferences sharedPreferences){
        String defaultListPrefValue = sharedPreferences.getString(getString(R.string.pref_list_key),
                getString(R.string.pref_list_red_value));
        mListPrefTextView.setText(defaultListPrefValue);
    }

    private void setEditPrefValue(SharedPreferences sharedPreferences){
        String defaultEditPrefValue = sharedPreferences.getString(getString(R.string.pref_edit_key),
                getString(R.string.pref_edit_default));
        mEditPrefTextView.setText(defaultEditPrefValue);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_check_key))){
            setCheckBoxValue(sharedPreferences);
        } else if(key.equals(getString(R.string.pref_list_key))){
            setListPrefValue(sharedPreferences);
        } else if(key.equals(getString(R.string.pref_edit_key))){
            setEditPrefValue(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pref_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_settings){
            Intent startSettingsActivityIntent = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivityIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
