package com.eden.orchid.api.options.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ImpliedKey(val value: String)
