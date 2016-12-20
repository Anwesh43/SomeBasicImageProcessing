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
        Drawable drawable = imageView.getDrawable();
        Bitmap carBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.car4);
        Bitmap bitmap = Bitmap.createBitmap(3*carBitmap.getWidth()/10,3*carBitmap.getHeight()/10,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.save();
        canvas.translate(bitmap.getWidth()/2,bitmap.getHeight()/2);
        canvas.scale(0.3f,0.3f);
        canvas.drawBitmap(carBitmap,-carBitmap.getWidth()/2,-carBitmap.getHeight()/2,paint);
        canvas.restore();
        ImageProcessingUtil.createGrayScaleImage(bitmap,imageView);
        ImageProcessingUtil.subtractRedChannelFromBitmap(bitmap,imageView1);
        //imageView.setImageBitmap(bitmap);
        //imageView.setImageBitmap(ImageProcessingUtil.createGrayScaleImage(bitmap));
    }
}
