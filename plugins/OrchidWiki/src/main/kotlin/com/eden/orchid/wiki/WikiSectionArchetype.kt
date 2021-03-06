package com.eden.orchid.wiki

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionArchetype
import com.eden.orchid.wiki.pages.WikiPage
import org.json.JSONObject
import javax.inject.Inject

class WikiSectionArchetype
@Inject
constructor(val context: OrchidContext) : OptionArchetype {

    override fun getOptions(target: Any, archetypeKey: String): Map<String, Any>? {
        if(target !is WikiPage) return null

        if(!EdenUtils.isEmpty(target.section)) {
            val contextOptions = context.query("$archetypeKey.${target.section}")
            if (EdenUtils.elementIsObject(contextOptions)) {
                return (contextOptions?.element as? JSONObject)?.toMap()
            }
        }

        return null
    }

}
