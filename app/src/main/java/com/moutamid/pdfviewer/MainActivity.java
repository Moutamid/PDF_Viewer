package com.moutamid.pdfviewer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.fxn.stash.Stash;
import com.moutamid.pdfviewer.databinding.ActivityMainBinding;
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig;
import com.nareshchocha.filepickerlibrary.ui.FilePicker;
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_PDF_REQUEST = 1001;
    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    String[] permissions13 = new String[]{
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        if (!Stash.getString(Constants.PATH, "").isEmpty()) {
            startActivity(new Intent(MainActivity.this, PdfActivity.class));
            finish();
        }

        binding.open.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (above13Check()) {
                    shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES);
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_VIDEO);
                    ActivityCompat.requestPermissions(MainActivity.this, permissions13, 2);
                } else {
                    open();
                }
            } else {
                if (below13Check()) {
                    shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, 2);
                } else {
                    open();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean above13Check() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean below13Check() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void open() {
        List<String> mMimeTypesList = new ArrayList<>();
        mMimeTypesList.add("application/pdf");
        launcher.launch(new FilePicker.Builder(this)
                .setPopUpConfig(null)
                .addPickDocumentFile(new DocumentFilePickerConfig(
                        null, // DrawableRes Id
                        null,// Title for pop item
                        true, // set Multiple pick file
                        null, // max files working only in android latest version
                        mMimeTypesList, // added Multiple MimeTypes
                        null,  // set Permission ask Title
                        null, // set Permission ask Message
                        null, // set Permission setting Title
                        null // set Permission setting Messag
                ))
                .build());
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("application/pdf");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    private static final String TAG = "MainActivity";

    private ActivityResultLauncher launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // Use the uri to load the image
                                Uri uri = result.getData().getData();
                                // Use the file path to set image or upload
                                String filePath = result.getData().getStringExtra(Const.BundleExtras.FILE_PATH);
                                Log.d(TAG, "filePath: " + filePath);
                                Stash.put(Constants.PATH, filePath);
                                startActivity(new Intent(MainActivity.this, PdfActivity.class));
                                finish();
                            }
                        }
                    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri path = data.getData();
            if (path != null) {
//                Log.d(TAG, "onActivityResult: " + path);
//                Stash.put(Constants.PATH, path.toString());
//                startActivity(new Intent(MainActivity.this, PdfActivity.class));
//                finish();
            }
        }
    }

//    private String getPathFromUri(Uri uri) {
//        String path = null;
//        if (DocumentsContract.isDocumentUri(this, uri)) {
//            String documentId = DocumentsContract.getDocumentId(uri);
//            if (documentId.startsWith("raw:")) {
//                path = documentId.substring(4);
//            } else {
//                String[] split = documentId.split(":");
//                if (split.length >= 2) {
//                    String type = split[0];
//                    String id = split[1];
//                    Uri contentUri = null;
//                    if ("primary".equalsIgnoreCase(type)) {
//                        contentUri = Uri.parse("content://media/external/file/" + id);
//                    } else {
//                        contentUri = Uri.parse("content://com.android.externalstorage.documents/document/" + id);
//                    }
//                    String[] projection = {DocumentsContract.MediaColumns.DATA};
//                    Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
//                    if (cursor != null && cursor.moveToFirst()) {
//                        int columnIndex = cursor.getColumnIndexOrThrow(DocumentsContract.MediaColumns.DATA);
//                        path = cursor.getString(columnIndex);
//                        cursor.close();
//                    }
//                }
//            }
//        }
//        return path;
//    }

    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // If the URI is a document URI
            DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);
            if (documentFile != null) {
                filePath = documentFile.getUri().getPath();
            }
        } else {
            // For older versions or non-document URIs
            String[] projection = {DocumentsContract.Document.COLUMN_DOCUMENT_ID};
            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    String documentId = cursor.getString(0);
                    documentId = documentId.split(":")[1];
                    String path = uri.getPath();
                    String externalPath = System.getenv("EXTERNAL_STORAGE");
                    if (path != null && externalPath != null) {
                        filePath = externalPath + path.substring(path.indexOf(documentId));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

}