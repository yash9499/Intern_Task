package com.example.pablo.contactlistextraction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ContactsModel model;
    private Disposable disposable;
    private CoordinatorLayout coordinatorLayout;
    ArrayList<ContactsModel> contacts;
    ContactsAdapter mContactsAdapter;
    String inputPath = "/data/data/com.example.pablo.contactlistextraction/files/";
    Cursor cursor;
    File file;
    String FILEName;
    private static int BUFFER = 8192;
    Button button1;
    String name, phonenumber;
    public static final int RequestPermissionCode = 1;
    public static final int RequestPermissionCode1 = 2;

    Button button;
    String inputFile = "Contacts.zip";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        File root = Environment.getExternalStorageDirectory();
        file = new File(root.getAbsolutePath(), "/contacts");
        if (EnableRuntimePermission()) {
            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_SHORT).show();
            if (!file.exists()) {

                file.mkdir();
                Log.v("error", file.getAbsolutePath() + file.exists());
            }

        } else {
            RequestMultiplePermission();
        }


        recyclerView = findViewById(R.id.list);
        button = findViewById(R.id.loadButton);
        button1 = findViewById(R.id.file);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        contacts = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                file.mkdir();
                getData();

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileToCsv();
                Log.v("error123", file.getAbsolutePath() + file.exists());
                Log.v("path", inputPath);
                zip(file.getAbsolutePath() + "/" + FILEName, file.getAbsolutePath() + "/" + inputFile);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Csv Zip File successfully Created", Snackbar.LENGTH_LONG);


                snackbar.show();
            }

        });
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS
                }, RequestPermissionCode);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @SuppressWarnings("ConstantConditions")
    public void getData() {
        //noinspection ConstantConditions
        disposable = getContactsListObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Cursor>() {
                    @Override
                    public void accept(Cursor cursor) throws Exception {
                        while (cursor.moveToNext()) {
                            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            model = new ContactsModel();
                            model.setName(name);
                            model.setPhoneNumber(phonenumber);
                            contacts.add(model);
                        }
                        cursor.close();
                        Log.v("load", contacts.size() + "");
                        mContactsAdapter = new ContactsAdapter(MainActivity.this, contacts);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        recyclerView.setAdapter(mContactsAdapter);
                    }

                });


    }

    private Observable<Cursor> getContactsListObservable() {

        //
        try {
            return Observable.just(getContactsList());
        } catch (Exception e) {
            Log.v("error", e.toString());
            return null;
        }
    }

    public Cursor getContactsList() {
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        return cursor;
    }

    public boolean EnableRuntimePermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CONTACTS);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String per[], @NonNull int[] grantResults) {
        Log.v("length", grantResults.length + "");

        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean ReadPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WritePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (ReadPermission && WritePermission) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public void fileToCsv() {
        FILEName = "contact_list.csv";
        //File file1= new File(file,FILEName);
        String entry;
        for (int i = 0; i < contacts.size(); i++) {
            entry = contacts.get(i).getName() + "," + contacts.get(i).getPhoneNumber() + "\n";
            try {
                FileOutputStream outputStream = openFileOutput(FILEName, Context.MODE_APPEND);
                // FileOutputStream outputStream = new FileOutputStream(file1);
                outputStream.write(entry.getBytes());
                outputStream.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (file.exists()) {

            InputStream in = null;
            try {
                in = new FileInputStream(inputPath + FILEName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            OutputStream out = null;
            try {
                out = new FileOutputStream(file.getAbsolutePath() + "/" + FILEName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;

            try {
                assert in != null;
                while ((len = in.read(buf)) > 0) {
                    assert out != null;
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.v("rdf", "CSV file successfully Placed.");

        } else {
            Log.v("rdf123", "Csv file placing failed.");
        }


    }

    public void zip(String _files, String zipFileName) {

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];


            Log.v("Compress", "Adding: " + _files);
            FileInputStream fi = new FileInputStream(_files);
            origin = new BufferedInputStream(fi, BUFFER);

            ZipEntry entry = new ZipEntry(_files.substring(_files.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();


            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (disposable == null || disposable.isDisposed()) {
            assert disposable != null;
            disposable.dispose();
        }
    }
}
