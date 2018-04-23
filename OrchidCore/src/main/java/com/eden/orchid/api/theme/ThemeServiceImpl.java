package com.eden.orchid.api.theme;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.assets.GlobalAssetHolder;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @orchidApi services
 * @since v1.0.0
 */
public final class ThemeServiceImpl implements ThemeService {

    private OrchidContext context;
    private final GlobalAssetHolder globalAssetHolder;

    private final String defaultTheme;
    private final String defaultAdminTheme;

    private Provider<Set<Theme>> themesProvider;
    private Provider<Set<AdminTheme>> adminThemesProvider;

    private final Map<String, Theme> loadedThemes;
    private final Map<String, AdminTheme> loadedAdminThemes;

    @Inject
    public ThemeServiceImpl(
            GlobalAssetHolder globalAssetHolder,
            Provider<Set<Theme>> themesProvider,
            @Named("theme") String defaultTheme,
            Provider<Set<AdminTheme>> adminThemesProvider,
            @Named("adminTheme") String defaultAdminTheme) {
        this.globalAssetHolder = globalAssetHolder;
        this.defaultTheme = defaultTheme;
        this.themesProvider = themesProvider;

        this.defaultAdminTheme = defaultAdminTheme;
        this.adminThemesProvider = adminThemesProvider;

        this.loadedThemes = new HashMap<>();
        this.loadedAdminThemes = new HashMap<>();
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public GlobalAssetHolder getGlobalAssetHolder() {
        return globalAssetHolder;
    }

// Interface Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void clearThemes() {
        loadedThemes.clear();
        loadedAdminThemes.clear();
        globalAssetHolder.clearAssets();
    }

    @Override
    public Theme findTheme() {
        return findTheme(defaultTheme);
    }

    @Override
    public Theme findTheme(String themeKey) {
        return loadedThemes.computeIfAbsent(themeKey, s -> findTheme(defaultTheme, null));
    }

    @Override
    public Theme findTheme(String themeKey, JSONObject data) {
        Theme foundTheme = themesProvider.get()
                .stream()
                .sorted()
                .filter(theme -> theme.getKey().equals(themeKey))
                .findFirst()
                .orElse(null);

        if (foundTheme != null) {
            Theme theme = context.getInjector().getInstance(foundTheme.getClass());

            JSONObject allThemeOptions = new JSONObject();

            JSONElement defaultThemeOptions = context.query("theme");
            if(EdenUtils.elementIsObject(defaultThemeOptions)) {
                allThemeOptions = OrchidUtils.merge(allThemeOptions, (JSONObject) defaultThemeOptions.getElement());
            }

            JSONElement customThemeOptions = context.query(themeKey);
            if(EdenUtils.elementIsObject(customThemeOptions)) {
                allThemeOptions = OrchidUtils.merge(allThemeOptions, (JSONObject) customThemeOptions.getElement());
            }

            if(data != null) {
                allThemeOptions = OrchidUtils.merge(allThemeOptions, data);
            }

            theme.clearCache();
            theme.initialize();
            theme.extractOptions(context, allThemeOptions);

            return theme;
        }
        else {
            Clog.e("Could not find theme [{}]", themeKey);
            return null;
        }
    }

    @Override
    public AdminTheme findAdminTheme() {
        return findAdminTheme(defaultAdminTheme);
    }

    @Override
    public AdminTheme findAdminTheme(String themeKey) {
        return loadedAdminThemes.computeIfAbsent(themeKey, s -> findAdminTheme(defaultAdminTheme, null));
    }

    @Override
    public AdminTheme findAdminTheme(String themeKey, JSONObject data) {
        AdminTheme foundTheme = adminThemesProvider.get()
                .stream()
                .sorted()
                .filter(theme -> theme.getKey().equals(themeKey))
                .findFirst()
                .orElse(null);

        if (foundTheme != null) {
            AdminTheme theme = context.getInjector().getInstance(foundTheme.getClass());

            JSONObject allThemeOptions = new JSONObject();

            JSONElement defaultThemeOptions = context.query("adminTheme");
            if(EdenUtils.elementIsObject(defaultThemeOptions)) {
                allThemeOptions = OrchidUtils.merge(allThemeOptions, (JSONObject) defaultThemeOptions.getElement());
            }

            JSONElement customThemeOptions = context.query(themeKey);
            if(EdenUtils.elementIsObject(customThemeOptions)) {
                allThemeOptions = OrchidUtils.merge(allThemeOptions, (JSONObject) customThemeOptions.getElement());
            }

            if(data != null) {
                allThemeOptions = OrchidUtils.merge(allThemeOptions, data);
            }

            theme.clearCache();
            theme.initialize();
            theme.extractOptions(context, allThemeOptions);

            return theme;
        }
        else {
            Clog.e("Could not find adminTheme [{}]", themeKey);
            return null;
        }
    }

}
