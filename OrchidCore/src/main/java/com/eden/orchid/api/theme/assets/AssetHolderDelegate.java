package com.eden.orchid.api.theme.assets;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.ExternalResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public final class AssetHolderDelegate implements AssetHolder {

    protected final OrchidContext context;

    public static final String JS_EXT = "js";
    public static final String CSS_EXT = "css";

    private final List<AssetPage> js;
    private final List<AssetPage> css;

    private Object source;
    private String sourceKey;

    private String prefix;
    private OrchidPage currentPage;

    @Inject
    public AssetHolderDelegate(OrchidContext context, Object source, String sourceKey) {
        this.context = context;
        this.source = source;
        this.sourceKey = sourceKey;
        this.js = new ArrayList<>();
        this.css = new ArrayList<>();
    }

    @Override
    public void addAssets(OrchidPage currentPage) {
        throw new UnsupportedOperationException("AssetHolderDelegate cannot add its own assets");
    }

    @Override
    public AssetHolder getAssetHolder() {
        return this;
    }

    @Override
    public AssetPage addJs(AssetPage jsAsset) {
        if(validAsset(jsAsset, JS_EXT)) {
            jsAsset.getReference().setUsePrettyUrl(false);
            js.add(jsAsset);
            context.getGlobalAssetHolder().addJs(jsAsset);
            return jsAsset;
        }
        else {
            Clog.w("#{$1} is not a valid JS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    jsAsset.getReference().getOriginalFullFileName(),
                    jsAsset.getReference().getOutputExtension(),
                    JS_EXT);
        }

        return null;
    }

    @Override
    public AssetPage addJs(String jsAsset) {
        OrchidResource resource = context.getResourceEntry(currentPage, jsAsset);
        if(resource != null) {
            boolean setPrefix = !EdenUtils.isEmpty(prefix);
            if(resource instanceof ExternalResource) {
                if(shouldDownloadExternalAssets()) {
                    ((ExternalResource) resource).setDownload(true);
                }
                else {
                    setPrefix = false;
                }
            }
            AssetPage page = new AssetPage(source, sourceKey, resource, FilenameUtils.getBaseName(jsAsset));
            if(setPrefix) {
                page.getReference().setPath(prefix + "/" + page.getReference().getOriginalPath());
            }
            addJs(page);
            return page;
        }
        else {
            Clog.w("Could not find JS asset: {}", jsAsset);
        }

        return null;
    }

    @Override
    public AssetPage addCss(AssetPage cssAsset) {
        if(validAsset(cssAsset, CSS_EXT)) {
            cssAsset.getReference().setUsePrettyUrl(false);
            css.add(cssAsset);
            context.getGlobalAssetHolder().addCss(cssAsset);
            return cssAsset;
        }
        else {
            Clog.w("#{$1} is not a valid CSS asset, perhaps you are missing a #{$2}->#{$3} Compiler extension?",
                    cssAsset.getReference().getOriginalFullFileName(),
                    cssAsset.getReference().getOutputExtension(),
                    CSS_EXT);
        }
        return null;
    }

    @Override
    public AssetPage addCss(String cssAsset) {
        OrchidResource resource = context.getResourceEntry(currentPage, cssAsset);
        if(resource != null) {
            boolean setPrefix = !EdenUtils.isEmpty(prefix);
            if(resource instanceof ExternalResource) {
                if(shouldDownloadExternalAssets()) {
                    ((ExternalResource) resource).setDownload(true);
                }
                else {
                    setPrefix = false;
                }
            }
            AssetPage page = new AssetPage(source, sourceKey, resource, FilenameUtils.getBaseName(cssAsset));
            if(setPrefix) {
                page.getReference().setPath(prefix + "/" + page.getReference().getOriginalPath());
            }
            addCss(page);
            return page;
        }
        else {
            Clog.w("Could not find CSS asset: {}", cssAsset);
        }

        return null;
    }

    @Override
    public List<AssetPage> getScripts() {
        return js;
    }

    @Override
    public List<AssetPage> getStyles() {
        return css;
    }

    @Override
    public void flushJs() {
        js.clear();
    }

    @Override
    public void flushCss() {
        css.clear();
    }

    @Override
    public void clearAssets() {
        flushJs();
        flushCss();
    }

    private boolean validAsset(OrchidPage asset, String targetExtension) {
        return asset.getReference().getOutputExtension().equalsIgnoreCase(targetExtension);
    }

    public boolean shouldDownloadExternalAssets() {
        return context.isProduction();
    }

    public void withNamespace(String namespace, Runnable cb) {
        prefix = OrchidUtils.normalizePath(namespace);
        cb.run();
        prefix = null;
    }

    public void withPage(OrchidPage currentPage, Runnable cb) {
        this.currentPage = currentPage;
        cb.run();
        this.currentPage = null;
    }
}
