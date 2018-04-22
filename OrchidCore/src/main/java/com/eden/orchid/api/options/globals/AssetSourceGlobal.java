package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.TemplateGlobal;
import com.eden.orchid.api.theme.assets.AssetPage;

public class AssetSourceGlobal implements TemplateGlobal<Object> {

    @Override
    public String key(Object page) {
        if(page instanceof AssetPage) {
            return ((AssetPage) page).getSourceKey();
        }
        return null;
    }

    @Override
    public Object get(OrchidContext context, Object page) {
        if(page instanceof AssetPage) {
            return ((AssetPage) page).getSource();
        }
        return null;
    }
}
