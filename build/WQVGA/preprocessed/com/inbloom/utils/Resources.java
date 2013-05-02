package com.inbloom.utils;

import com.uikit.globalization.ResourceManager;
import java.io.IOException;
import javax.microedition.lcdui.Image;

public class Resources {

    private ResourceManager manager;
    
    private ResourceManager theme;
    
    private static Resources instance;

    private Resources() {
    }

    public static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();
        }
        return instance;
    }

    public void initResources(String resourceBundleName, String bundleName) throws IOException {
        manager = new ResourceManager(resourceBundleName, bundleName);
    }
    
    public void initTheme(String resourceBundleName, String theme) throws IOException{
        this.theme = new ResourceManager(resourceBundleName, theme);
    }

    public String getText(int id) {
        return manager.getStringResource(id);
    }
    
    public Image getThemeImage(int id){
        byte[] imgData = this.theme.getBinaryResource(id);
        return Image.createImage(imgData, 0, imgData.length);
    }
    
    public String getThemeStr(int id){
        return this.theme.getStringResource(id);
    }
}
