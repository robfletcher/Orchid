package com.eden.orchid.api.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;

public final class AdminAssetResponse {

    private final OrchidContext context;

    @Inject
    public AdminAssetResponse(OrchidContext context) {
        this.context = context;
    }

    public OrchidResponse getResponse(File targetFile, String targetPath) {
        Theme theme = context.getTheme(context.getDefaultAdminTheme());

        OrchidResource res = theme.getResourceEntry(targetPath);
        String mimeType = StaticFileResponse.mimeTypes.getOrDefault(FilenameUtils.getExtension(targetFile.getName()), "text/plain");

        Clog.i("Rendering admin File: #{$1}", targetPath);
        if (res != null) {
            if(context.isBinaryExtension(FilenameUtils.getExtension(targetFile.getName()))) {
                InputStream stream = res.getContentStream();

                return new OrchidResponse(context)
                        .contentStream(stream)
                        .mimeType(mimeType);
            }
            else {
                OrchidPage page = new OrchidPage(res, "");

                return new OrchidResponse(context)
                        .mimeType(mimeType)
                        .content(page.getContent())
                        .mimeType(mimeType);
            }
        }

        return null;
    }
}
