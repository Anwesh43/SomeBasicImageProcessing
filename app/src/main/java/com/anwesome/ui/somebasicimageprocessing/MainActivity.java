package com.anwesome.ui.somebasicimageprocessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.anwesome.ui.basicimageprocessing.ImageProcessingUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView)findViewById(R.id.imv);
        ImageView imageView1 = (ImageView)findViewById(R.id.imv1);
        ImageView imageView2 = (ImageView)findViewById(R.id.imv2);
        Drawable drawable = imageView.getDrawable();
        Bitmap carBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.car4);
        Bitmap bitmap = ImageProcessingUtil.compressBitmapToScale(carBitmap,0.7f);
        ImageProcessingUtil.createGrayScaleImage(bitmap,imageView);
        ImageProcessingUtil.subtractRedChannelFromBitmap(bitmap,imageView1);
        ImageProcessingUtil.applySepiaFilter(bitmap,imageView2);
    }
}
