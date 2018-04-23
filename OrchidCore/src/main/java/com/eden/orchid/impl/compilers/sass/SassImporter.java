package com.eden.orchid.impl.compilers.sass;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.utilities.OrchidUtils;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SassImporter implements Importer {

    private final OrchidContext context;

    @Inject
    public SassImporter(OrchidContext context) {
        this.context = context;
    }

    @Override
    public Collection<Import> apply(String url, Import previous) {
        EdenPair<String, String> thisItem = splitPath(url);

        String[] availableFiles = new String[] {
                thisItem.first + "/" + thisItem.second + ".scss",
                thisItem.first + "/" + thisItem.second + ".sass",
                thisItem.first + "/" + "_" + thisItem.second + ".scss",
                thisItem.first + "/" + "_" + thisItem.second + ".sass"
        };

        for (String availableFile : availableFiles) {
            String pathStart = OrchidUtils.normalizePath(splitPath(previous.getAbsoluteUri().getPath()).first);
            String pathEnd = OrchidUtils.normalizePath(availableFile);
            String absoluteUri = OrchidUtils.normalizePath(pathStart + "/" + pathEnd);

            if (absoluteUri.contains("//")) {
                absoluteUri = absoluteUri.replaceAll("//", "/");
            }
            if (absoluteUri.startsWith("/")) {
                absoluteUri = absoluteUri.substring(1);
            }

            //TODO: How do I get this theme key from here?
//            String themeKey = "BsDoc";
            Theme theme = context.findTheme();
            OrchidResource importedResource = context.getResourceEntry(theme, "assets/css/" + absoluteUri);

            if (importedResource != null) {
                String content = importedResource.getContent();

                if (importedResource.shouldPrecompile()) {
                    JSONElement el = importedResource.getEmbeddedData();
                    Map<String, Object> data;
                    if(EdenUtils.elementIsObject(el)) {
                        JSONObject ob = (JSONObject) el.getElement();
                        data = ob.toMap();
                        data.put("theme", theme);
                    }
                    else {
                        data = new HashMap<>();
                        data.put("theme", theme);
                    }

                    content = context.compile(importedResource.getPrecompilerExtension(), content, data);
                }

                try {
                    String newURI = "" + OrchidUtils.normalizePath(absoluteUri);
                    Import newImport = new Import(newURI, newURI, content);
                    return Collections.singletonList(newImport);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private EdenPair<String, String> splitPath(String name) {
        name = name.replaceAll("\\\\\\\\", "/");
        name = name.replaceAll("\\\\", "/");

        if (name.contains("/")) {
            String[] pieces = name.split("/");
            String path = "";

            for (int i = 0; i < pieces.length - 1; i++) {
                path += pieces[i].replaceAll("_", "") + "/";
            }
            String fileName = pieces[pieces.length - 1].replaceAll("_", "");

            return new EdenPair<>(OrchidUtils.normalizePath(path), OrchidUtils.normalizePath(fileName));
        }
        else {
            return new EdenPair<>("", name);
        }
    }
}
