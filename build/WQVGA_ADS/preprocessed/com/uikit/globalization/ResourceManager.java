package com.uikit.globalization;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ResourceManager {

    private Bundle currentBundle;
    private Bundle commonBundle;
    private String resourceBundleCollectionName;

    public ResourceManager(String resourceBundleCollectionName, String resourceBundleName) throws IOException {

        if (resourceBundleName == null || resourceBundleCollectionName == null) {
            throw new IllegalArgumentException();
        }

        this.resourceBundleCollectionName = resourceBundleCollectionName;

        loadResourceBundle(resourceBundleName);

    }

    public String getStringResource(int resourceId) {
        try {
            return (String) currentBundle.getStringResource(resourceId);
        } catch (Exception e) {
            try {
                return (String) commonBundle.getStringResource(resourceId);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public byte[] getBinaryResource(int resourceId) {
        if (currentBundle.getResource(resourceId) != null) {
            return (byte[]) currentBundle.getResource(resourceId);
        } else {
            return (byte[]) commonBundle.getResource(resourceId);
        }
    }

    private void loadResourceBundle(String bundle_name) throws IOException {
        InputStream is = null;
        is = ResourceManager.class.getResourceAsStream("/global/" + bundle_name + "/" + resourceBundleCollectionName + ".res");
        if (is != null) {
            currentBundle = new Bundle(bundle_name);
            currentBundle.deSerialise(is);
        } else {
            throw new IOException("File " + "/global/" + bundle_name + "/" + resourceBundleCollectionName + ".res cannot be loaded.");
        }

        if (commonBundle == null) {
            InputStream isBundle = ResourceManager.class.getResourceAsStream("/global/" + resourceBundleCollectionName + ".res");
            commonBundle = new Bundle("common");
            commonBundle.deSerialise(isBundle);
        }
    }
}
