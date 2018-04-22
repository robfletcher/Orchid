package com.eden.orchid.api.resources;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.AbstractTheme;
import com.google.inject.ImplementedBy;
import org.json.JSONObject;

import java.util.List;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@ImplementedBy(ResourceServiceImpl.class)
public interface ResourceService extends OrchidService {

    default JSONObject getDatafile(final String fileName) {
        return getService(ResourceService.class).getDatafile(fileName);
    }

    default JSONObject getDatafiles(final String directory) {
        return getService(ResourceService.class).getDatafiles(directory);
    }

    default OrchidResource getLocalResourceEntry(final String fileName) {
        return getService(ResourceService.class).getLocalResourceEntry(fileName);
    }

    default OrchidResource getResourceEntry(final String fileName) {
        return getService(ResourceService.class).getResourceEntry(fileName);
    }

    default OrchidResource getResourceEntry(AbstractTheme theme, final String fileName) {
        return getService(ResourceService.class).getResourceEntry(theme, fileName);
    }

    default List<OrchidResource> getLocalResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getService(ResourceService.class).getLocalResourceEntries(path, fileExtensions, recursive);
    }

    default List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        return getService(ResourceService.class).getResourceEntries(path, fileExtensions, recursive);
    }

    default List<OrchidResource> getResourceEntries(AbstractTheme theme, String path, String[] fileExtensions, boolean recursive) {
        return getService(ResourceService.class).getResourceEntries(theme, path, fileExtensions, recursive);
    }

    default JSONObject loadAdditionalFile(String url) {
        return getService(ResourceService.class).loadAdditionalFile(url);
    }

    default JSONObject loadLocalFile(String url) {
        return getService(ResourceService.class).loadLocalFile(url);
    }

    default JSONObject loadRemoteFile(String url) {
        return getService(ResourceService.class).loadRemoteFile(url);
    }

    default OrchidResource findClosestFile(String filename) {
        return getService(ResourceService.class).findClosestFile(filename);
    }

    default OrchidResource findClosestFile(String filename, boolean strict) {
        return getService(ResourceService.class).findClosestFile(filename, strict);
    }

    default OrchidResource findClosestFile(String filename, boolean strict, int maxIterations) {
        return getService(ResourceService.class).findClosestFile(filename, strict, maxIterations);
    }

    default OrchidResource locateLocalResourceEntry(final String fileName) {
        return getService(ResourceService.class).locateLocalResourceEntry(fileName);
    }

    default OrchidResource locateLocalResourceEntry(final String fileName, String[] fileExtensions) {
        return getService(ResourceService.class).locateLocalResourceEntry(fileName, fileExtensions);
    }

    default OrchidResource locateLocalResourceEntry(final String fileName, List<String> fileExtensions) {
        return getService(ResourceService.class).locateLocalResourceEntry(fileName, fileExtensions);
    }

    default OrchidResource locateTemplate(AbstractTheme theme, final String fileNames) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames);
    }

    default OrchidResource locateTemplate(AbstractTheme theme, final String fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames, ignoreMissing);
    }

    default OrchidResource locateTemplate(AbstractTheme theme, final String[] fileNames) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames);
    }

    default OrchidResource locateTemplate(AbstractTheme theme, final List<String> fileNames) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames);
    }

    default OrchidResource locateTemplate(AbstractTheme theme, final String[] fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames, ignoreMissing);
    }

    default OrchidResource locateTemplate(AbstractTheme theme, final List<String> fileNames, boolean ignoreMissing) {
        return getService(ResourceService.class).locateTemplate(theme, fileNames, ignoreMissing);
    }

}
