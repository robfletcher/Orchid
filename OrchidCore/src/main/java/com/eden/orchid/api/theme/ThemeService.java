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

    default String getDefaultAdminTheme() {
        return getService(ThemeService.class).getDefaultAdminTheme();
    }

    default String getDefaultTheme() {
        return getService(ThemeService.class).getDefaultTheme();
    }

    default Theme getTheme(String themeKey) {
        return getService(ThemeService.class).getTheme(themeKey);
    }

    default Theme getTheme(String themeKey, JSONObject data) {
        return getService(ThemeService.class).getTheme(themeKey, data);
    }

    default void clearThemes() {
        getService(ThemeService.class).clearThemes();
    }

}
