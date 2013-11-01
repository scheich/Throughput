package de.mangelow.throughput;
/***
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may
* not use this file except in compliance with the License. You may obtain
* a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.view.Window;

public class MainActivity extends PreferenceActivity {

	private Context context;
	private Resources res;

	private Intent serviceIntent;

	public static final String PREF_FILE = "Prefs";

	public static final String ENABLED = "enabled";
	public static final boolean ENABLED_DEFAULT = false;

	public static final String SHOWTICKER = "showticker";
	public static final boolean SHOWTICKER_DEFAULT = true;

	public static final String SHOWBITSORBYTES = "showbitsorbytes";
	public static final boolean SHOWBITSORBYTES_DEFAULT = true;

	public static final String SHOWSSIDSUBTYPE = "showssidsubtype";
	public static final boolean SHOWSSIDSUBTYPE_DEFAULT = true;

	public static final String SHOWSIGNALSTRENGTH = "showsignalstrength";
	public static final boolean SHOWSIGNALSTRENGTH_DEFAULT = false;

	public static final String SHOWWIFILINKSPEED = "showwifilinkspeed";
	public static final boolean SHOWWIFILINKSPEED_DEFAULT = true;

	public static final String SHOWIPADDRESS = "showipaddress";
	public static final boolean SHOWIPADDRESS_DEFAULT = true;

	public static final String SHOWCELLS = "showcells";
	public static final boolean SHOWCELLS_DEFAULT = false;

	public static final String SHOWONAIRPLANEMODE = "showonairplanemode";
	public static final boolean SHOWONAIRPLANEMODE_DEFAULT = true;

	public static final String REFRESH = "refresh";
	public static final int REFRESH_DEFAULT = 2;

	public static final String ONTAP = "ontap";
	public static final int ONTAP_DEFAULT = 0;
	
	public static final String THRESHOLD = "threshold";
	public static final int THRESHOLD_DEFAULT = 3;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if(Build.VERSION.SDK_INT<11)requestWindowFeature(Window.FEATURE_LEFT_ICON);
		super.onCreate(savedInstanceState);

	}
	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();

		context = getApplicationContext();
		res = context.getResources();

		serviceIntent = new Intent(context, NotificationService.class);

		setPreferenceScreen(createPreferences());

		if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB)getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.ic_launcher);
	}
	@SuppressWarnings("deprecation")
	private PreferenceScreen createPreferences() {

		final PreferenceScreen root = getPreferenceManager().createPreferenceScreen(context);	
		final PreferenceCategory pc_settings = new PreferenceCategory(context);

		boolean enabled = loadBooleanPref(context, ENABLED, ENABLED_DEFAULT);
		if(enabled&&!isMyServiceRunning(context)) {
			startService(serviceIntent);	

		}

		final CheckBoxPreference cbp_enabled = new CheckBoxPreference(context);
		cbp_enabled.setTitle(res.getString(R.string.enable));
		cbp_enabled.setSummary(res.getString(R.string.enable_text));
		cbp_enabled.setChecked(enabled);
		cbp_enabled.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());

				if(newvalue) {
					startService(serviceIntent);
				}
				else {
					stopService(serviceIntent);					
				}

				saveBooleanPref(context, ENABLED, newvalue);

				pc_settings.setEnabled(newvalue);				

				return true;
			}
		});
		root.addPreference(cbp_enabled);

		//

		boolean showticker = MainActivity.loadBooleanPref(context, MainActivity.SHOWTICKER, MainActivity.SHOWTICKER_DEFAULT);
		boolean showipaddress = MainActivity.loadBooleanPref(context, MainActivity.SHOWIPADDRESS, MainActivity.SHOWIPADDRESS_DEFAULT);
		boolean showssidsubtype = MainActivity.loadBooleanPref(context, MainActivity.SHOWSSIDSUBTYPE, MainActivity.SHOWSSIDSUBTYPE_DEFAULT);
		boolean showcells = MainActivity.loadBooleanPref(context, MainActivity.SHOWCELLS, MainActivity.SHOWCELLS_DEFAULT);
		boolean showwifilinkspeed = MainActivity.loadBooleanPref(context, MainActivity.SHOWWIFILINKSPEED, MainActivity.SHOWWIFILINKSPEED_DEFAULT);
		boolean showsignalstrength = MainActivity.loadBooleanPref(context, MainActivity.SHOWSIGNALSTRENGTH, MainActivity.SHOWSIGNALSTRENGTH_DEFAULT);
		boolean showonairplanemode = MainActivity.loadBooleanPref(context, MainActivity.SHOWONAIRPLANEMODE, MainActivity.SHOWONAIRPLANEMODE_DEFAULT);
		boolean showbitsorbytes = MainActivity.loadBooleanPref(context, MainActivity.SHOWBITSORBYTES, MainActivity.SHOWBITSORBYTES_DEFAULT);

		pc_settings.setEnabled(enabled);
		root.addPreference(pc_settings);

		final CheckBoxPreference cbp_showticker = new CheckBoxPreference(context);
		cbp_showticker.setTitle(res.getString(R.string.showticker));
		cbp_showticker.setSummary(res.getString(R.string.showticker_text));
		cbp_showticker.setChecked(showticker);
		cbp_showticker.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWTICKER, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showticker);

		final CheckBoxPreference cbp_showssidsubtype = new CheckBoxPreference(context);
		cbp_showssidsubtype.setTitle(res.getString(R.string.showssidsubtype));
		cbp_showssidsubtype.setSummary(res.getString(R.string.showssidsubtype_text));
		cbp_showssidsubtype.setChecked(showssidsubtype);
		cbp_showssidsubtype.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWSSIDSUBTYPE, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showssidsubtype);

		final CheckBoxPreference cbp_showipaddress = new CheckBoxPreference(context);
		cbp_showipaddress.setTitle(res.getString(R.string.showipaddress));
		cbp_showipaddress.setSummary(res.getString(R.string.showipaddress_text));
		cbp_showipaddress.setChecked(showipaddress);
		cbp_showipaddress.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWIPADDRESS, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showipaddress);

		final CheckBoxPreference cbp_showcells = new CheckBoxPreference(context);
		cbp_showcells.setTitle(res.getString(R.string.showcells));
		cbp_showcells.setSummary(res.getString(R.string.showcells_text));
		cbp_showcells.setChecked(showcells);
		cbp_showcells.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWCELLS, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showcells);

		final CheckBoxPreference cbp_showsignalstrength = new CheckBoxPreference(context);
		cbp_showsignalstrength.setTitle(res.getString(R.string.showsignalstrength));
		cbp_showsignalstrength.setSummary(res.getString(R.string.showsignalstrength_text));
		cbp_showsignalstrength.setChecked(showsignalstrength);
		cbp_showsignalstrength.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWSIGNALSTRENGTH, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showsignalstrength);

		final CheckBoxPreference cbp_showwifilinkspeed = new CheckBoxPreference(context);
		cbp_showwifilinkspeed.setTitle(res.getString(R.string.showwifilinkspeed));
		cbp_showwifilinkspeed.setSummary(res.getString(R.string.showwifilinkspeed_text));
		cbp_showwifilinkspeed.setChecked(showwifilinkspeed);
		cbp_showwifilinkspeed.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWWIFILINKSPEED, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showwifilinkspeed);

		final CheckBoxPreference cbp_showonairplanemode = new CheckBoxPreference(context);
		cbp_showonairplanemode.setTitle(res.getString(R.string.showonairplanemode));
		cbp_showonairplanemode.setSummary(res.getString(R.string.showonairplanemode_text));
		cbp_showonairplanemode.setChecked(showonairplanemode);
		cbp_showonairplanemode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWONAIRPLANEMODE, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showonairplanemode);

		final CheckBoxPreference cbp_showbitsorbytes = new CheckBoxPreference(context);
		cbp_showbitsorbytes.setTitle(res.getString(R.string.showbitsorbytes));
		cbp_showbitsorbytes.setSummary(res.getString(R.string.showbitsorbytes_text));
		cbp_showbitsorbytes.setChecked(showbitsorbytes);
		cbp_showbitsorbytes.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference p, Object o) {
				boolean newvalue = Boolean.parseBoolean(o.toString());
				saveBooleanPref(context, SHOWBITSORBYTES, newvalue);				
				return true;
			}
		});
		pc_settings.addPreference(cbp_showbitsorbytes);

		int refresh = loadIntPref(context, REFRESH, REFRESH_DEFAULT);

		final String [] refresh_entries = res.getStringArray(R.array.refresh);
		int length_refresh_entries = refresh_entries.length;
		
		String [] refresh_values = new String[length_refresh_entries];
		for (int i = 0; i < length_refresh_entries; i++)refresh_values[i] = String.valueOf(i);

		ListPreference lp_refresh = new ListPreference(this);
		lp_refresh.setTitle(res.getString(R.string.refresh));
		lp_refresh.setEntries(refresh_entries);
		lp_refresh.setEntryValues(refresh_values);
		lp_refresh.setSummary(refresh_entries[refresh]);
		lp_refresh.setValue(String.valueOf(refresh));
		lp_refresh.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				final String summary = newValue.toString();
				ListPreference lp = (ListPreference) preference;	
				int newvalue = lp.findIndexOfValue(summary);
				lp.setSummary(refresh_entries[newvalue]);
				saveIntPref(context, REFRESH, newvalue);

				return true;
			}
		}); 
		pc_settings.addPreference(lp_refresh);
		
		int ontap = loadIntPref(context, ONTAP, ONTAP_DEFAULT);

		final String [] ontap_entries = res.getStringArray(R.array.ontap);
		int length_ontap_entries = ontap_entries.length;
		
		String [] ontap_values = new String[length_ontap_entries];
		for (int i = 0; i < length_ontap_entries; i++)ontap_values[i] = String.valueOf(i);

		ListPreference lp_ontap = new ListPreference(this);
		lp_ontap.setTitle(res.getString(R.string.ontap));
		lp_ontap.setEntries(ontap_entries);
		lp_ontap.setEntryValues(ontap_values);
		lp_ontap.setSummary(ontap_entries[ontap]);
		lp_ontap.setValue(String.valueOf(ontap));
		lp_ontap.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				final String summary = newValue.toString();
				ListPreference lp = (ListPreference) preference;	
				int newvalue = lp.findIndexOfValue(summary);
				lp.setSummary(ontap_entries[newvalue]);
				saveIntPref(context, ONTAP, newvalue);

				return true;
			}
		}); 
		pc_settings.addPreference(lp_ontap);

		int threshold = loadIntPref(context, THRESHOLD, THRESHOLD_DEFAULT);

		final String [] threshold_entries = res.getStringArray(R.array.threshold);
		int length_threshold_entries = threshold_entries.length;
		
		String [] threshold_values = new String[length_threshold_entries];
		for (int i = 0; i < length_threshold_entries; i++)threshold_values[i] = String.valueOf(i);

		ListPreference lp_threshold = new ListPreference(this);
		lp_threshold.setTitle(res.getString(R.string.threshold));
		lp_threshold.setEntries(threshold_entries);
		lp_threshold.setEntryValues(threshold_values);
		lp_threshold.setSummary(threshold_entries[threshold]);
		lp_threshold.setValue(String.valueOf(threshold));
		lp_threshold.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				final String summary = newValue.toString();
				ListPreference lp = (ListPreference) preference;	
				int newvalue = lp.findIndexOfValue(summary);
				lp.setSummary(threshold_entries[newvalue]);
				saveIntPref(context, THRESHOLD, newvalue);

				return true;
			}
		}); 
		pc_settings.addPreference(lp_threshold);
		
		//

		return root;
	}

	public static void saveBooleanPref(Context context,String name, boolean value) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(PREF_FILE, 0).edit();
		prefs.putBoolean(name, value);
		prefs.commit();
	}
	public static Boolean loadBooleanPref(Context context, String name, boolean defaultvalue) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
		boolean bpref = prefs.getBoolean(name, defaultvalue);
		return bpref;
	}	
	public static void saveIntPref(Context context,String name, int value) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(PREF_FILE, 0).edit();
		prefs.putInt(name, value);
		prefs.commit();
	}
	public static int loadIntPref(Context context, String name, int defaultvalue) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, 0);
		int lpref = prefs.getInt(name, defaultvalue);
		return lpref;
	}
	public boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("de.mangelow.throughput.NotificationService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
