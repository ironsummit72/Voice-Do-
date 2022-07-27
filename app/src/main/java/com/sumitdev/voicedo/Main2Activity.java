package com.sumitdev.voicedo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class Main2Activity extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks {
   private Toolbar toolbar;
private FloatingActionButton floatingActionButtonAddNote;
private Dialog dialog,textdialog;
  private   Button save,cancel,textsave,textcancel;
   private ImageView startrecord;
    private TextView texts,duration,titletext,addnote;
    boolean bt = false;
    private boolean recordstarted = false;
    SqliteDatabase sqliteDatabase;
    private MediaRecorder mRecorder;
    Adapter adapter;
    private Handler mHandler = new Handler();
    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            tick();
            mHandler.postDelayed(mTickExecutor, 100);
        }
    };
    private File mOutputFile;
    private long mStartTime = 0;
    private int[] amplitudes = new int[100];
    String path = "";
    private int i = 0;
    private RecyclerView recyclerView;
    List<Content>todolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButtonAddNote=(FloatingActionButton)findViewById(R.id.floatingActionButtonAddNote);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
addnote=(TextView)findViewById(R.id.addnote);
sqliteDatabase=new SqliteDatabase(this);
todolist=new ArrayList<>();

        setSupportActionBar(toolbar);
        openrecordpermission();


        Cursor cursor = sqliteDatabase.retrivedata();
        while (cursor.moveToNext()) {
            int uid=cursor.getInt(0);

            String todotx = cursor.getString(1);
            String path = cursor.getString(2);
            String booleancheck = cursor.getString(3);



            Content content = new Content(todotx,path,booleancheck,uid);

            adapter = new Adapter(todolist, this);
            recyclerView.setAdapter(adapter);

            todolist.add(content);
        }


        if (todolist.size() > 0) {
            addnote.setVisibility(View.INVISIBLE);
        } else {
            addnote.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);






        floatingActionButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
openvoicedialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menutoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intent = new Intent(Main2Activity.this, Settings.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tos(String ss) {
        Toast.makeText(Main2Activity.this, ss, Toast.LENGTH_SHORT).show();
    }


    @AfterPermissionGranted(123)
    private void openrecordpermission() {
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because will store your audio with your todo list",
                    123, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }
    private void openvoicedialog()
    {


        dialog = new Dialog(this, R.style.Dialog);

        dialog.setContentView(R.layout.voicealert);

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        save=(Button)dialog.findViewById(R.id.save);
        cancel=(Button)dialog.findViewById(R.id.cancel);
        startrecord=(ImageView)dialog.findViewById(R.id.imageViewStop);
        texts=(TextView)dialog.findViewById(R.id.textch);
        duration=(TextView)dialog.findViewById(R.id.duration);
        startrecord.setBackground(getResources().getDrawable(R.drawable.ic_mic_green_24dp));
        startrecord.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (bt == false) {

            startrecord.setBackground(getResources().getDrawable(R.drawable.ic_stop_black_24dp));

            startRecording();
            save.setEnabled(true);
            recordstarted = true;


            texts.setText("Tap to Stop Recording");
            bt = true;
        } else {

            texts.setText("Tap to Start  Recording");
            startrecord.setBackground(getResources().getDrawable(R.drawable.ic_mic_green_24dp));
            openTextDialog();


        }
    }



});
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recordstarted) {
                    dialog.dismiss();

                    recreate();
                } else {
                    stopRecording(false);
                    setResult(RESULT_CANCELED);
                    recreate();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTextDialog();
            }
        });

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mRecorder.setAudioEncodingBitRate(48000);
        } else {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(64000);
        }
        mRecorder.setAudioSamplingRate(16000);
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartTime = SystemClock.elapsedRealtime();
            mHandler.postDelayed(mTickExecutor, 100);
            Log.d("Voice Recorder", "started recording to " + mOutputFile.getAbsolutePath());

        } catch (IOException e) {
            Log.e("Voice Recorder", "prepare() failed " + e.getMessage());

        }
    }

    protected void stopRecording(boolean saveFile) {
        mRecorder.stop();
        // mRecorder.release();
        mRecorder = null;

        mStartTime = 0;
        mHandler.removeCallbacks(mTickExecutor);
        if (!saveFile && mOutputFile != null) {
            mOutputFile.delete();

            tos("Canceled");

        }
    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        path = Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/VoiceDo/Vdo _"
                + dateFormat.format(new Date())
                + ".m4a";
        return new File(path);

    }

    private void tick() {
        long time = (mStartTime < 0) ? 0 : (SystemClock.elapsedRealtime() - mStartTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        int milliseconds = (int) (time / 100) % 10;
        duration.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + "." + milliseconds);
        if (mRecorder != null) {
            amplitudes[i] = mRecorder.getMaxAmplitude();
            //Log.d("Voice Recorder","amplitude: "+(amplitudes[i] * 100 / 32767));
            if (i >= amplitudes.length - 1) {
                i = 0;
            } else {
                ++i;
            }
        }


    }
    private void openTextDialog()
    {
        pauserec();
        stopRecording(true);
        textdialog = new Dialog(this);
        textdialog.setContentView(R.layout.titlealert);
        textdialog.show();
        Window window = textdialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        titletext=(EditText)textdialog.findViewById(R.id.titletext);
        textsave=(Button)textdialog.findViewById(R.id.textsave);
        textcancel=(Button)textdialog.findViewById(R.id.textcancel);
        textsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String title= titletext.getText().toString();
            if (titletext.getText().toString().isEmpty())
            {
                tos("Enter Title");
            }
                else {  long dd= sqliteDatabase.insertdata(title,path,"UNCHECKED");
                textdialog.dismiss();
                dialog.dismiss();}
                recreate();


            }
        });
        textcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording(false);
                setResult(RESULT_CANCELED);
                textdialog.dismiss();
                dialog.dismiss();
                recreate();
            }
        });



    }
    public void pauserec() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mRecorder.pause();
            mHandler.removeCallbacks(mTickExecutor);
        }
    }

}


