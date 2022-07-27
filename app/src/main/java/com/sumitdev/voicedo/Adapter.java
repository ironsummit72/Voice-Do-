package com.sumitdev.voicedo;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.PlaceHolder> {
    List<Content> todolist;
    Context context;
    SqliteDatabase data;
    MediaPlayer mediaPlayer;
    SeekBar progressseek;
    Runnable runnable;
    Handler handler;
    TextView current, total;


    public Adapter(List<Content> todolist, Context context) {
        this.todolist = todolist;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter.PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listlayout, parent, false);

        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.PlaceHolder holder, final int position) {
        data = new SqliteDatabase(context);
        handler = new Handler();
        //   int databaseid=todolist.get(position).uid;
        holder.todotxtview.setText(todolist.get(position).todo);
        final String path = todolist.get(position).path;
        String checkuncheck = todolist.get(position).booleancheck;
        if (checkuncheck.equals("UNCHECKED")) {
            holder.checkBox.setChecked(false);

        }
        if (checkuncheck.equals("CHECKED")) {
            holder.checkBox.setChecked(true);
            holder.todotxtview.setPaintFlags(holder.todotxtview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setClickable(false);

        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    data = new SqliteDatabase(context);

                    holder.todotxtview.setPaintFlags(holder.todotxtview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    int s = todolist.get(position).uid;


                    data.updatedata(s, "CHECKED");
                    toast("TASK COMPLETED");
                    Log.d("position info", "" + s);
                    // Log.d("Updated to Position",""+rowid);


                } else if (!isChecked) {
                    holder.todotxtview.setPaintFlags(holder.todotxtview.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    int f = todolist.get(position).uid;
                    data.updatedata(f, "UNCHECKED");

                    Log.d("position info", "" + f);
                }
            }
        });
        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = new SqliteDatabase(context);
                data.deletedata(todolist.get(position).uid);
                File file = new File(todolist.get(position).path);
                file.delete();

                toast("Deleted restart to see changes");


            }
        });
        holder.cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogplayread(path, holder.todotxtview.getText().toString());
            }
        });


    }

    public void dialogplayread(final String url, final String title) {
        final Button play, read;

        final Dialog playreaddialog = new Dialog(context);
        playreaddialog.setContentView(R.layout.play_read);
        play = playreaddialog.findViewById(R.id.play);
        read = playreaddialog.findViewById(R.id.read);
        playreaddialog.show();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playdialog(url);

                playreaddialog.dismiss();
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                readdialog(title);

                playreaddialog.dismiss();
            }
        });

        Window window = playreaddialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);


    }

    public void playdialog(String url) {
        final Button dplay, dpause, dstop;
        final TextView status;


        Dialog mediadialog = new Dialog(context);

        mediadialog.setContentView(R.layout.play_dialog);
        progressseek = mediadialog.findViewById(R.id.seekBarprogress);
        dplay = mediadialog.findViewById(R.id.playpause);
        current = mediadialog.findViewById(R.id.currentd);
        total = mediadialog.findViewById(R.id.totald);
        // status=(TextView)mediadialog.findViewById(R.id.status);


        mediadialog.show();
      /*  Window window = mediadialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);*/
        mediaPlayer = new MediaPlayer();


        try {

            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    progressseek.setMax(mediaPlayer.getDuration());
                    dplay.setBackground(context.getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    play();
                    //  status.setText("Playing......");

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        dplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    dplay.setBackground(context.getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                    // status.setText("Playing......");
                } else if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    dplay.setBackground(context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));

                    //status.setText("paused......");
                }
            }
        });

        progressseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);//this will seek to the music to the desired using seekbar
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void readdialog(String s) {
        TextView reads;

        Dialog readdialog = new Dialog(context);
        readdialog.setContentView(R.layout.read);
        reads = readdialog.findViewById(R.id.reads);
        reads.setMovementMethod(new ScrollingMovementMethod());
        reads.setText(s);
        readdialog.show();
        Window window = readdialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);


    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";


        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }


        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;


        return finalTimerString;


    }

    public void play() {

        progressseek.setProgress(mediaPlayer.getCurrentPosition());
        runnable = new Runnable() {
            @Override
            public void run() {


                long currentduration = mediaPlayer.getCurrentPosition();
                long totalduration = mediaPlayer.getDuration();
                total.setText("" + milliSecondsToTimer(totalduration));
                current.setText("" + milliSecondsToTimer(currentduration));


                play();
            }


        };
        handler.postDelayed(runnable, 100);
    }


    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public void toast(String s) {
        Toast.makeText(context, "" + s, Toast.LENGTH_SHORT).show();

    }

    public class PlaceHolder extends RecyclerView.ViewHolder {
        private final TextView todotxtview;
        private final CheckBox checkBox;
        private final Button deletebutton;
        private final CardView cards;


        public PlaceHolder(@NonNull View itemView) {


            super(itemView);

            todotxtview = itemView.findViewById(R.id.todotxtview);
            cards = itemView.findViewById(R.id.cards);
            checkBox = itemView.findViewById(R.id.checkBox);
            deletebutton = itemView.findViewById(R.id.deletebutton);
        }

    }


}







