/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.settings.testutils.shadow;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.HashMap;
import java.util.Map;

@Implements(AccountManager.class)
public class ShadowAccountManager{

    private static final Map<String, AuthenticatorDescription> sAuthenticators = new HashMap<>();

    @Implementation
    public AuthenticatorDescription[] getAuthenticatorTypesAsUser(int userId) {
        return sAuthenticators.values().toArray(new AuthenticatorDescription[sAuthenticators.size()]);
    }

    public static void addAuthenticator(AuthenticatorDescription authenticator) {
        sAuthenticators.put(authenticator.type, authenticator);
    }

    public static void resetAuthenticator() {
        sAuthenticators.clear();
    }
}
