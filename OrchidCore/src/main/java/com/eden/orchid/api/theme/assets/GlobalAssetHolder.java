package com.eden.orchid.api.theme.assets;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class GlobalAssetHolder {

    private final Provider<OrchidContext> context;

    private final List<AssetPage> assets;

    @Inject
    public GlobalAssetHolder(Provider<OrchidContext> context) {
        this.context = context;
        this.assets = new ArrayList<>();
    }

    // Assets should only make it here if it passes the check in a local AssetHolderDelegate, so we don't need to check
    // again. Go ahead and render it now so we can free its resources, and eventually implement an asset pipeline from
    // this point
    public AssetPage addAsset(AssetPage asset) {
        assets.add(asset);
        if(context.get().isBinaryExtension(asset.getReference().getOutputExtension())) {
            context.get().renderBinary(asset);
        }
        else {
            context.get().renderRaw(asset);
        }

        return asset;
    }

    public void clearAssets() {
        assets.clear();
    }

}
