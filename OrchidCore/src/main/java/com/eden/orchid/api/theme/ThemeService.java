package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.assets.GlobalAssetHolder;
import com.google.inject.ImplementedBy;
import org.json.JSONObject;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(ThemeServiceImpl.class)
public interface ThemeService extends OrchidService {

    default GlobalAssetHolder getGlobalAssetHolder() {
        return getService(ThemeService.class).getGlobalAssetHolder();
    }

    default Theme findTheme() {
        return getService(ThemeService.class).findTheme();
    }

    default Theme findTheme(String themeKey) {
        return getService(ThemeService.class).findTheme(themeKey);
    }

    default Theme findTheme(String themeKey, JSONObject data) {
        return getService(ThemeService.class).findTheme(themeKey, data);
    }

    default void clearThemes() {
        getService(ThemeService.class).clearThemes();
    }

    default AdminTheme findAdminTheme() {
        return getService(ThemeService.class).findAdminTheme();
    }

    default AdminTheme findAdminTheme(String themeKey) {
        return getService(ThemeService.class).findAdminTheme(themeKey);
    }

    default AdminTheme findAdminTheme(String themeKey, JSONObject data) {
        return getService(ThemeService.class).findAdminTheme(themeKey, data);
    }
}
