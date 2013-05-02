package com.inbloom;

//#if QVGA_ADS
import InneractiveSDK.IADView;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
//#endif

public class AdManager {
    //#if QVGA_ADS
    private IAdListener listener;
    // True when advert is being downloaded
    private boolean isDownloading;
    private MIDlet appMidlet;
    private String appID;
    private Hashtable optionalParams;

    public AdManager(IAdListener listener, MIDlet appMidlet, String appID, Hashtable optionalParams) {
        this.listener = listener;
        this.appMidlet = appMidlet;
        this.appID = appID;
        this.optionalParams = optionalParams;
    }

    public void downloadAd() {
        if (!isDownloading) {
            if (listener != null) {
                listener.onAdDownloadStart();
            }
            
            isDownloading = true;
            Vector advertInfo = IADView.getBannerAdData(appMidlet, appID, optionalParams);
            if (null != advertInfo) {
                Image image = (Image) advertInfo.elementAt(0);
                String clickUrl = (String) advertInfo.elementAt(1);
                if (listener != null) {
                    listener.onAdDownloaded(image, clickUrl);
                }
            } else {
                if (listener != null) {
                    listener.onAdDownloadError();
                }
            }

            isDownloading = false;
        }
    }

    public interface IAdListener {

        void onAdDownloaded(Image image, String clickUrl);

        void onAdDownloadStart();

        void onAdDownloadError();
    }
    
    //#endif
}
