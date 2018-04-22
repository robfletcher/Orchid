package com.eden.orchid.api.theme;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.OptionsData;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.api.theme.assets.ThemeAssetHolder;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractTheme extends PluginResourceSource implements OptionsHolder, AssetHolder {

    @Getter protected final String key;
    @Getter protected final AssetHolder assetHolder;

    @Getter @Setter @OptionsData
    private JSONElement allData;

    @Getter @Setter
    @Option
    @Description("Add extra CSS files to every page rendered with this theme, which will be compiled just like the " +
            "rest of the site's assets."
    )
    protected String[] extraCss;

    @Getter @Setter
    @Option
    @Description("Add extra Javascript files to every page rendered with this theme, which will be compiled just " +
            "like the rest of the site's assets."
    )
    protected String[] extraJs;

    private boolean hasAddedAssets;

    @Getter @Setter private boolean hasRenderedAssets;

    @Getter @Setter protected String preferredTemplateExtension;

    @Getter @Setter private OrchidPage currentPage;

    public AbstractTheme(OrchidContext context, String key, int priority) {
        super(context, priority);
        this.key = key;
        this.assetHolder = new ThemeAssetHolder(context, this);
        this.preferredTemplateExtension = "peb";
    }

    public void clearCache() {
        assetHolder.clearAssets();
        hasAddedAssets = false;
        hasRenderedAssets = false;
    }

    public void initialize() {

    }

    @Override
    public final void addAssets(OrchidPage currentPage) {
        if(!hasAddedAssets) {
            withPage(currentPage, () ->
                withNamespace(getKey() + "/" + Integer.toHexString(this.hashCode()), () -> {
                    loadAssets();
                    OrchidUtils.addExtraAssetsTo(currentPage, context, extraCss, extraJs, this, this, "theme");
                })
            );
            hasAddedAssets = true;
        }
    }

    protected void loadAssets() {

    }

    public final void renderAssets() {
        if (!isHasRenderedAssets()) {
            getScripts()
                    .stream()
                    .forEach(context::renderRaw);
            getStyles()
                    .stream()
                    .forEach(context::renderRaw);
            hasRenderedAssets = true;
        }
    }

    @Override
    public final List<AssetPage> getScripts() {
        addAssets(currentPage);
        List<AssetPage> scripts = new ArrayList<>();
        scripts.addAll(assetHolder.getScripts());
        OrchidUtils.addComponentAssets(currentPage, getComponentHolders(), scripts, OrchidComponent::getScripts);

        return scripts;
    }

    @Override
    public final List<AssetPage> getStyles() {
        addAssets(currentPage);
        List<AssetPage> styles = new ArrayList<>();
        styles.addAll(assetHolder.getStyles());
        OrchidUtils.addComponentAssets(currentPage, getComponentHolders(), styles, OrchidComponent::getStyles);

        return styles;
    }

    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTheme)) return false;
        if (!super.equals(o)) return false;
        AbstractTheme that = (AbstractTheme) o;
        if(getAllData() != null) {
            return Objects.equals(getKey(), that.getKey()) &&
                Objects.equals(getAllData().getElement(), that.getAllData().getElement());
        }
        else {
            return Objects.equals(getKey(), that.getKey());
        }
    }

    @Override
    public int hashCode() {
        if(getAllData() != null) {
            return Objects.hash(super.hashCode(), getKey(), getAllData().getElement());
        }
        else {
            return Objects.hash(super.hashCode(), getKey());
        }
    }
}
