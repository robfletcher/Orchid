package com.eden.orchid.api.theme.assets;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class GlobalAssetHolder implements AssetHolder {

    private final Provider<OrchidContext> context;

    private final List<AssetPage> js;
    private final List<AssetPage> css;

    @Inject
    public GlobalAssetHolder(Provider<OrchidContext> context) {
        this.context = context;
        this.js = new ArrayList<>();
        this.css = new ArrayList<>();
    }

    @Override
    public void addAssets(OrchidPage currentPage) {
        throw new UnsupportedOperationException("GlobalAssetHolder cannot add its own assets");
    }

    @Override
    public AssetHolder getAssetHolder() {
        return this;
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check again
    @Override
    public AssetPage addJs(AssetPage jsAsset) {
        js.add(jsAsset);
        return jsAsset;
    }

    @Override
    public AssetPage addJs(String jsAsset) {
        throw new UnsupportedOperationException("Assets should not be added directly to the GlobalAssetHolder, add them " +
                "to a Theme, Page, or Component instead.");
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check again
    @Override
    public AssetPage addCss(AssetPage cssAsset) {
        css.add(cssAsset);
        return cssAsset;
    }

    @Override
    public AssetPage addCss(String cssAsset) {
        throw new UnsupportedOperationException("Assets should not be added directly to the GlobalAssetHolder, add them " +
                "to a Theme, Page, or Component instead.");
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

    public void setShouldDownloadAssets(boolean shouldDownloadAssets) {

    }

    public boolean shouldDownloadExternalAssets() {
        return context.get().isProduction();
    }

    public void withNamespace(String namespace, Runnable cb) {
        throw new UnsupportedOperationException("Cannot set the namespace on the GlobalAssetHolder");
    }

    public void withPage(OrchidPage currentPage, Runnable cb) {
        throw new UnsupportedOperationException("Cannot set the currentPage on the GlobalAssetHolder");
    }
}
