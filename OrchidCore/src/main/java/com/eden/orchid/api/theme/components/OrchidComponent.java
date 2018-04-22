package com.eden.orchid.api.theme.components;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.OptionsData;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import javax.inject.Inject;
import java.util.List;

/**
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Extensible
public abstract class OrchidComponent extends Prioritized implements OptionsHolder, AssetHolder, ModularListItem<ComponentHolder, OrchidComponent> {

    protected final OrchidContext context;

    @Getter protected final String type;
    @Getter protected final AssetHolder assetHolder;
    private boolean hasAddedAssets;

    @Setter protected OrchidPage page;

    @Getter @Setter
    @Option
    @Description("Specify a list of templates to use when rendering this component. The first template that exists " +
            "will be chosen for this component."
    )
    protected String[] templates;

    @Getter @Setter
    @Option @IntDefault(0)
    @Description("By default, components are rendered in the order in which they are declared, but the ordering can " +
            "be changed by setting the order on any individual component. A higher value for order will render that " +
            "component earlier in the list."
    )
    protected int order;

    @Getter @Setter
    @Option
    @Description("Add extra CSS files to the page containing this Component, which will be compiled just like the " +
            "rest of the site's assets."
    )
    protected String[] extraCss;

    @Getter @Setter
    @Option
    @Description("Add extra Javascript files to the page containing this Component, which will be compiled just like " +
            "the rest of the site's assets."
    )
    protected String[] extraJs;

    @Getter @Setter
    @Option @BooleanDefault(false)
    @Description("When true, this component will not have a template rendered on the page. Useful for Components that" +
            " only add extra CSS or JS, or for temporarily removing a component from the page."
    )
    protected boolean hidden;

    @Getter @Setter
    @Option @BooleanDefault(false)
    @Description("When true, this component will not be wrapped in a wrapper element. The wrapper element is determined" +
            "by the theme, and it is up to the theme to ensure this is implemented properly."
    )
    protected boolean noWrapper;

    @Getter @Setter
    @OptionsData
    private JSONElement allData;

    @Inject
    public OrchidComponent(OrchidContext context, String type, int priority) {
        super(priority);
        this.type = type;
        this.context = context;
        this.assetHolder = new AssetHolderDelegate(context, this, "component");
    }

    @Override
    public boolean canBeUsedOnPage(
            OrchidPage containingPage,
            ComponentHolder componentHolder,
            JSONArray possibleComponents,
            List<OrchidComponent> currentComponents) {
        return true;
    }

    @Override
    public final void addAssets(OrchidPage currentPage) {
        if(!hasAddedAssets) {
            withPage(currentPage, () -> {
                loadAssets();
                OrchidUtils.addExtraAssetsTo(currentPage, context, extraCss, extraJs, this, this, "component");
                hasAddedAssets = true;
            });
        }
    }

    @Override
    public final List<AssetPage> getScripts() {
        addAssets(page);
        return assetHolder.getScripts();
    }

    @Override
    public final List<AssetPage> getStyles() {
        addAssets(page);
        return assetHolder.getStyles();
    }

    protected void loadAssets() {

    }
}
