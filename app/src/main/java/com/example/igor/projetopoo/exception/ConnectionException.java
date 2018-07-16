package com.example.igor.projetopoo.exception;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.igor.projetopoo.R;
import com.example.igor.projetopoo.activity.main.MainActivity;
import com.example.igor.projetopoo.activity.main.MainMVP;
import com.example.igor.projetopoo.activity.main.MainPresenter;

public class ConnectionException extends Exception {
    private Context context;
    private Activity activity;
    private ConstraintLayout layout;

    public ConnectionException(Context context, ConstraintLayout layout) {
        this.context = context;
        this.activity = (Activity) context;
        this.layout = layout;
    }

    public void connectionFail(final Object object) {
        final View view = LayoutInflater.from(context).inflate(R.layout.connection_fail, layout, false);

        Button button = view.findViewById(R.id.try_again_connection);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (object instanceof MainPresenter) {
                    MainMVP.PresenterOps presenterOps = (MainMVP.PresenterOps) object;

                    presenterOps.getCategoryList();
                }
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layout.addView(view, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            }
        });

    }
}
