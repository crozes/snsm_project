package com.cyrilcrozes.simulboat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Enregistrement extends CreateScenario {
    ImageView im;
    Button bArret;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enregistrement);

        Animation amB = AnimationUtils.loadAnimation(this,R.anim.blink_anim);
        bArret = (Button) findViewById(R.id.bArret);

        im = (ImageView) findViewById(R.id.iEnregistrement);
        im.setImageResource(R.drawable.logo);

        amB.setDuration(1300);

        im.startAnimation(amB);

        amB.setRepeatCount(Animation.INFINITE);

        bArret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
