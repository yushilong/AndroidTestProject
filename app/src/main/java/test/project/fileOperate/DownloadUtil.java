package test.project.fileOperate;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by yushilong on 2015/10/14.
 */
public class DownloadUtil {
    DownloadManager.Request request;
    Context context;
    long downloadId;
    DownloadManager downloadManager;
    String uri;
    String fileDir;
    String fileName;

    public DownloadUtil(Context context, String uri, String fileDir, String fileName) {
        this.context = context;
        this.uri = uri;
        this.fileDir = fileDir;
        this.fileName = fileName;
        this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        init();
    }

    public DownloadManager.Request getRequest() {
        return request;
    }

    public void init() {
        request = new DownloadManager.Request(Uri.parse(uri));
        //set default config to request
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            request.setAllowedOverMetered(false);
        }
        request.setVisibleInDownloadsUi(false);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        String dir = FileUtil.FOLDER_PREFIX + File.separator + fileDir;
        File file = new File(Environment.getExternalStoragePublicDirectory(dir), fileName);
        if (file.exists())
            file.delete();
        request.setDestinationInExternalPublicDir(dir, fileName);
    }

    public void start() {
        if (request == null)
            return;
        downloadId = downloadManager.enqueue(request);
    }

    //如果想取消下载，则可以调用remove方法完成，此方法可以将下载任务和已经下载的文件同时删除
    public void remove(long downloadId) {
        downloadManager.remove(downloadId);
    }
}
