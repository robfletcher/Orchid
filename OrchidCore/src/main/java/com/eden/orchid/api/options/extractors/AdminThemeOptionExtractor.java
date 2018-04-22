package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.theme.AdminTheme;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class AdminThemeOptionExtractor extends OptionExtractor<AdminTheme> {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public AdminThemeOptionExtractor(Provider<OrchidContext> contextProvider) {
        super(1000);
        this.contextProvider = contextProvider;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return AdminTheme.class.equals(clazz);
    }

    @Override
    public AdminTheme getOption(Field field, Object sourceObject, String key) {
        return null;
    }

    @Override
    public AdminTheme getDefaultValue(Field field) {
        return contextProvider.get().findAdminTheme();
    }

}
