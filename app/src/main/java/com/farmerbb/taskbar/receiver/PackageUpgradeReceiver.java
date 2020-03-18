/* Copyright 2016 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.taskbar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.farmerbb.taskbar.activity.DummyActivity;
import com.farmerbb.taskbar.service.NotificationService;
import com.farmerbb.taskbar.util.U;

public class PackageUpgradeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
            SharedPreferences pref = U.getSharedPreferences(context);
            boolean startServices = false;

            if(pref.getBoolean("taskbar_active", false) && !pref.getBoolean("is_hidden", false)) {
                if(U.hasFreeformSupport(context) && pref.getBoolean("freeform_hack", false)) {
                    Intent intent2 = new Intent(context, DummyActivity.class);
                    intent2.putExtra("start_freeform_hack", true);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent2);
                }

                startServices = true;
            }

            if(pref.getBoolean("taskbar_active", false)) {
                Intent notificationIntent = new Intent(context, NotificationService.class);
                notificationIntent.putExtra("start_services", startServices);

                U.startForegroundService(context, notificationIntent);
            }
        }
    }
}
