package com.eden.orchid.api.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class IndexFileResponse {

    private final OrchidContext context;
    private final AssetHolder assetHolder;
    private final Map<String, String> iconMap;

    @Inject
    public IndexFileResponse(OrchidContext context) {
        this.context = context;
        assetHolder = new AssetHolderDelegate(context, null, null);

        this.iconMap = new HashMap<>();

        iconMap.put("css",    "assets/svg/css.svg");
        iconMap.put("csv",    "assets/svg/csv.svg");
        iconMap.put("html",   "assets/svg/html.svg");
        iconMap.put("jpg",    "assets/svg/jpg.svg");
        iconMap.put("js",     "assets/svg/js.svg");
        iconMap.put("json",   "assets/svg/json.svg");
        iconMap.put("png",    "assets/svg/png.svg");
        iconMap.put("xml",    "assets/svg/xml.svg");
        iconMap.put("file",   "assets/svg/folder.svg");
        iconMap.put("folder", "assets/svg/folder.svg");
    }

    public OrchidResponse getResponse(File targetFile, String targetPath) {
        String content = "";

        if (targetFile.isDirectory()) {
            Clog.i("Rendering directory index: {}", targetPath);
            File[] files = targetFile.listFiles();

            if (files != null) {
                JSONArray jsonDirs = new JSONArray();
                JSONArray jsonFiles = new JSONArray();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss z", Locale.getDefault());

                for (File file : files) {
                    JSONObject newFile = new JSONObject();
                    newFile.put("url", OrchidUtils.applyBaseUrl(context, StringUtils.strip(targetPath, "/") + "/" + file.getName()));
                    newFile.put("name", file.getName());
                    newFile.put("size", file.length());
                    newFile.put("date", formatter.format(new Date(file.lastModified())));
                    
                    String icon;

                    if (file.isDirectory()) {
                        icon = iconMap.get("folder");
                        jsonDirs.put(newFile);
                    }
                    else {
                        icon = iconMap.containsKey(FilenameUtils.getExtension(file.getName()))
                                ? iconMap.get(FilenameUtils.getExtension(file.getName()))
                                : iconMap.get("file");
                        jsonFiles.put(newFile);
                    }
                    newFile.put("icon", icon);
                    assetHolder.addAsset(icon);
                }

                OrchidResource resource = context.getResourceEntry("templates/server/directoryListing.peb");

                JSONObject indexPageVars = new JSONObject();
                indexPageVars.put("title", "List of files/dirs under " + targetPath);
                indexPageVars.put("path", targetPath);
                indexPageVars.put("dirs", jsonDirs);
                indexPageVars.put("files", jsonFiles);

                JSONObject object = new JSONObject(context.getOptionsData().toMap());
                object.put("page", indexPageVars);

                String directoryListingContent;
                if (resource != null) {
                    directoryListingContent = context.compile(resource.getReference().getExtension(), resource.getContent(), object.toMap());
                }
                else {
                    directoryListingContent = object.toString(2);
                }

                OrchidPage page = new OrchidPage(new StringResource(context, "directoryListing.txt", directoryListingContent), "directoryListing");
                page.addJs("assets/js/shadowComponents.js");
                assetHolder.addCss("assets/css/directoryListing.css");

                InputStream is = context.getRenderedTemplate(page);
                try {
                    content = IOUtils.toString(is, Charset.defaultCharset());
                }
                catch (Exception e) {
                    content = "";
                }
            }
        }

        return new OrchidResponse(context).content(content);
    }
}
