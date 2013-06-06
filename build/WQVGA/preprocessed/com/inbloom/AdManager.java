package com.inbloom;

//#if ADS
//# import InneractiveSDK.IADView;
//# import java.util.Hashtable;
//# import java.util.Vector;
//# import javax.microedition.lcdui.Image;
//# import javax.microedition.midlet.MIDlet;
//#endif

public class AdManager 
    //#if ADS
//# implements Runnable {
    //#else
    {
    //#endif
//#if ADS
//#     private IAdListener listener;
//#     // True when advert is being downloaded
//#     private boolean startDownload;
//#     private final MIDlet appMidlet;
//#     private String appID;
//#     private Hashtable optionalParams;
//#     private final Object lock = new Object();
//# 
//#     public AdManager(IAdListener listener, MIDlet appMidlet, String appID, Hashtable optionalParams) {
//#         this.listener = listener;
//#         this.appMidlet = appMidlet;
//#         this.appID = appID;
//#         this.optionalParams = optionalParams;
//#         new Thread(this).start();
//#     }
//# 
//#     public void downloadAd() {
//#         synchronized (lock) {
//#             this.startDownload = true;
//#             lock.notify();
//#         }
//#     }
//# 
//#     public void run() {
//#         while (true) {
//#             synchronized (lock) {
//#                 if (startDownload) {
//#                     if (listener != null) {
//#                         listener.onAdDownloadStart();
//#                     }
//#                     Vector advertInfo = IADView.getBannerAdData(appMidlet, appID, optionalParams);
//#                     if (null != advertInfo) {
//#                         Image image = (Image) advertInfo.elementAt(0);
//#                         String clickUrl = (String) advertInfo.elementAt(1);
//#                         if (listener != null) {
//#                             listener.onAdDownloaded(image, clickUrl);
//#                         }
//#                     } else {
//#                         if (listener != null) {
//#                             listener.onAdDownloadError();
//#                         }
//#                     }
//# 
//#                     startDownload = false;
//#                 }
//#                 
//#                 try {
//#                     lock.wait();
//#                 } catch (Exception e) {
//#                     e.printStackTrace();
//#                 }
//#             }
//#         }
//#     }
//# 
//#     public interface IAdListener {
//# 
//#         void onAdDownloaded(Image image, String clickUrl);
//# 
//#         void onAdDownloadStart();
//# 
//#         void onAdDownloadError();
//#     }
    //#endif
}
