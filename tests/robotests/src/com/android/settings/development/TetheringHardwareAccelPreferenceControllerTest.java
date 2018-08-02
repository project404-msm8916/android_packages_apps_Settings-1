/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.settings.development;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.provider.Settings;

import com.android.settings.testutils.SettingsRobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

@RunWith(SettingsRobolectricTestRunner.class)
public class TetheringHardwareAccelPreferenceControllerTest {

    @Mock
    private SwitchPreference mPreference;
    @Mock
    private PreferenceScreen mPreferenceScreen;

    private Context mContext;
    private TetheringHardwareAccelPreferenceController mController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mContext = RuntimeEnvironment.application;
        mController = new TetheringHardwareAccelPreferenceController(mContext);
        when(mPreferenceScreen.findPreference(mController.getPreferenceKey()))
            .thenReturn(mPreference);
        mController.displayPreference(mPreferenceScreen);
    }

    @Test
    public void onPreferenceChanged_settingEnabled_turnOnTetheringAccel() {
        mController.onPreferenceChange(mPreference, true /* new value */);

        final int mode = Settings.System.getInt(mContext.getContentResolver(),
                Settings.Global.TETHER_OFFLOAD_DISABLED, -1 /* default */);

        assertThat(mode).isEqualTo(TetheringHardwareAccelPreferenceController.SETTING_VALUE_ON);
    }

    @Test
    public void onPreferenceChanged_settingDisabled_turnOffTetheringAccel() {
        mController.onPreferenceChange(mPreference, false /* new value */);

        final int mode = Settings.System.getInt(mContext.getContentResolver(),
                Settings.Global.TETHER_OFFLOAD_DISABLED, -1 /* default */);

        assertThat(mode).isEqualTo(TetheringHardwareAccelPreferenceController.SETTING_VALUE_OFF);
    }

    @Test
    public void updateState_settingEnabled_preferenceShouldBeChecked() {
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.Global.TETHER_OFFLOAD_DISABLED,
                TetheringHardwareAccelPreferenceController.SETTING_VALUE_ON);
        mController.updateState(mPreference);

        verify(mPreference).setChecked(true);
    }

    @Test
    public void updateState_settingDisabled_preferenceShouldNotBeChecked() {
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.Global.TETHER_OFFLOAD_DISABLED,
                TetheringHardwareAccelPreferenceController.SETTING_VALUE_OFF);
        mController.updateState(mPreference);

        verify(mPreference).setChecked(false);
    }

    @Test
    public void onDeveloperOptionsSwitchDisabled_shouldDisablePreference() {
        mController.onDeveloperOptionsSwitchDisabled();
        final int mode = Settings.System.getInt(mContext.getContentResolver(),
                Settings.Global.TETHER_OFFLOAD_DISABLED, -1 /* default */);

        assertThat(mode).isEqualTo(TetheringHardwareAccelPreferenceController.SETTING_VALUE_OFF);
        verify(mPreference).setEnabled(false);
        verify(mPreference).setChecked(false);
    }
}
