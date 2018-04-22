package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.TemplateGlobal;
import com.eden.orchid.api.site.OrchidSite;

public class SiteGlobal implements TemplateGlobal<OrchidSite> {

    @Override
    public String key(Object page) {
        return "site";
    }

    @Override
    public OrchidSite get(OrchidContext context, Object page) {
        return context.getSite();
    }
}
