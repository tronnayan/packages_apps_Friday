/*
 * Copyright (C) 2020 NarvisOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.narvis.settings.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.narvis.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.narvis.NarvisUtils;

public class FingerprintPrefs extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String FP_SWIPE_CALL_ACTIONS = "fp_swipe_call_actions";

    private ListPreference mFpSwipeCallActions;

    private int mFpSwipeCallActionsValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fingerprint_prefs);
        PreferenceScreen prefSet = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();

        if (getResources().getBoolean(com.android.internal.R.bool.config_supportSystemNavigationKeys)) {
            mFpSwipeCallActions = (ListPreference) findPreference(FP_SWIPE_CALL_ACTIONS);
            mFpSwipeCallActionsValue = Settings.System.getIntForUser(getContentResolver(),
                    Settings.System.FP_SWIPE_CALL_ACTIONS, 0, UserHandle.USER_CURRENT);
            mFpSwipeCallActions.setValue(Integer.toString(mFpSwipeCallActionsValue));
            mFpSwipeCallActions.setSummary(mFpSwipeCallActions.getEntry());
            mFpSwipeCallActions.setOnPreferenceChangeListener(this);
        } else {
            prefSet.removePreference(mFpSwipeCallActions);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.NARVISOS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mFpSwipeCallActions) {
            mFpSwipeCallActionsValue = Integer.valueOf((String) objValue);
            int index = mFpSwipeCallActions.findIndexOfValue((String) objValue);
            mFpSwipeCallActions.setSummary(
                    mFpSwipeCallActions.getEntries()[index]);
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.FP_SWIPE_CALL_ACTIONS, mFpSwipeCallActionsValue,
                    UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }
}
