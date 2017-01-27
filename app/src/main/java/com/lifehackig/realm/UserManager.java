package com.lifehackig.realm;

import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

/**
 * Created by Sheena on 1/26/17.
 */

public class UserManager {
    public static void logoutActiveUser() {
        SyncUser.currentUser().logout();
    }

    // Configure Realm for the current active user
    public static void setActiveUser(SyncUser user) {
        SyncConfiguration defaultConfig = new SyncConfiguration.Builder(user, RealmApplication.REALM_URL).build();
        Realm.setDefaultConfiguration(defaultConfig);
    }

    public static String getCurrentUserId() {
        return SyncUser.currentUser().getIdentity();
    }
}
