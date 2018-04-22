package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;

/**
 * An object that is globally available to all Pages when they are rendered.
 *
 * @since v1.0.0
 * @orchidApi extensible
 * @param <T> the object type that is added globally
 */
public interface TemplateGlobal<T> {

    String key(Object page);

    T get(OrchidContext context, Object page);

}
