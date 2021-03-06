package com.eden.orchid.pages

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.pages.menu.PageIdsMenuType
import com.eden.orchid.pages.menu.PagesMenuType
import com.eden.orchid.utilities.addToSet

class PagesModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidGenerator, PagesGenerator>()
        addToSet<OrchidMenuItem>(
                PagesMenuType::class,
                PageIdsMenuType::class)
    }
}

