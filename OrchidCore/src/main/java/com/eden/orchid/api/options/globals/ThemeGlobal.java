package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.TemplateGlobal;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;

public class ThemeGlobal implements TemplateGlobal<Theme> {

    @Override
    public String key(Object page) {
        return "theme";
    }

    @Override
    public Theme get(OrchidContext context, Object source) {
        return (source instanceof OrchidPage) ? ((OrchidPage) source).getTheme() : null;
    }
}
