package com.eden.orchid.impl.themes.functions;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

@Getter @Setter
public final class TemplatesFunction extends TemplateFunction {

    private final OrchidContext context;

    @Option
    @Description("The object to format.")
    private Object input;

    @Option
    @Description("The templates to look up.")
    private String[] templates;

    @Option
    @Description("Whether to ignore when the templates are missing.")
    private boolean ignoreMissing;

    @Inject
    public TemplatesFunction(OrchidContext context) {
        super("templates", false);
        this.context = context;
    }

    @Override
    public String[] parameters() {
        return new String[] {"input", "templates", "ignoreMissing"};
    }

    @Override
    public Object apply(Object input) {
        if(!(input instanceof OrchidPage)) {
            throw new IllegalArgumentException("Input must be used as a filter, and the input must be the current Page");
        }
        OrchidPage page = (OrchidPage) input;

        return Clog.format(
                "#{$1}::#{$2}#{$3|join(',')}",
                page.getTheme().getKey(),
                (ignoreMissing) ? "?" : "",
                templates);
    }

}
