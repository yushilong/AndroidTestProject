package test.project;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

import test.project.fileOperate.DownloadUtil;
import test.project.fileOperate.FileUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String fileDirName = "mobile";
    private String fileName = "index.html";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new Handler();
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                testJump();
            }
        });
        final WebView webView = (WebView) findViewById(R.id.webView);
        Button loadUrl = (Button) findViewById(R.id.loadUrl);
        loadUrl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String dir = FileUtil.FOLDER_PREFIX + File.separator + fileDirName;
                File file = new File(Environment.getExternalStoragePublicDirectory(dir), fileName);
                if (file.exists())
                    webView.loadUrl("file:///" + file.getAbsolutePath());
            }
        });
        Button unzip = (Button) findViewById(R.id.unzip);
        unzip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileDir = FileUtil.FOLDER_PREFIX + File.separator + fileDirName;
                final String targetDir = fileDir + File.separator + "unzip";
                final File file = new File(FileUtil.getFileDir(fileDir), "app-release.zip");
                if (file.exists()) {
                    final File targetfile = FileUtil.getFileDir(targetDir);
                    Toast.makeText(LoginActivity.this, "解压文件开始", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FileUtil.deleteDir(targetfile);
                                targetfile.mkdirs();
                                FileUtil.unzip(LoginActivity.this, handler, file.getPath(), targetDir);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        final Button getVersion = (Button) findViewById(R.id.getVersion);
        getVersion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileDir = FileUtil.FOLDER_PREFIX + File.separator + fileDirName;
                final String targetDir = fileDir + File.separator + "unzip";
                final File file = new File(FileUtil.getFileDir(targetDir), "version.txt");
                try {
                    String version = Files.toString(file, Charsets.UTF_8);
                    getVersion.setText(version);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void testJump() {
        DownloadUtil downloadUtil = new DownloadUtil(this, "http://shihuo.hupucdn.com/app/shihuo_app_3.1.0.apk", fileDirName, fileName);
        downloadUtil.start();
    }
}

