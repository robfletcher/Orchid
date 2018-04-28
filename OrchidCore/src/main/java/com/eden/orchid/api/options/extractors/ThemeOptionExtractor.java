package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.server.OrchidView;
import com.eden.orchid.api.theme.Theme;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class ThemeOptionExtractor extends OptionExtractor<Theme> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;

    @Inject
    public ThemeOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter) {
        super(1000);
        this.contextProvider = contextProvider;
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return Theme.class.equals(clazz);
    }

    @Override
    public Theme getOption(Field field, Object sourceObject, String key) {
//        String themeKey = converter.convert(sourceObject).second;
//        if(!EdenUtils.isEmpty(themeKey)) {
//            Clog.v("Using theme: {}", themeKey);
//            Theme theme = contextProvider.get().findTheme(themeKey);
//            if(theme != null) {
//                Clog.v("Using theme: {} (not null, {}, {})", themeKey, theme.getClass().getName(), theme.getKey());
//                return theme;
//            }
//        }

        return null;
    }

    @Override
    public Theme getDefaultValue(Field field) {
        String themeKey = (OrchidView.class.isAssignableFrom(field.getDeclaringClass()))
                ? contextProvider.get().getDefaultAdminTheme()
                : contextProvider.get().getDefaultTheme();

        return contextProvider.get().getTheme(themeKey);
    }

}
