package com.anwesome.ui.basicimageprocessing;

import android.graphics.*;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by anweshmishra on 19/12/16.
 */
public class ImageProcessingUtil {
    private static Handler handler = new Handler();
    private static MainpulationHandler grayScaleManipulator = new MainpulationHandler() {
        @Override
        public void manipulateImage(Bitmap bitmap, int x, int y, int pixel) {
            float gs = 0.16f*getRed(pixel)+0.34f*getGreen(pixel)+0.25f*getBlue(pixel);
            bitmap.setPixel(x,y,Color.argb(getAlpha(pixel),(int)gs,(int)gs,(int)gs));
        }
    };
    private static MainpulationHandler subtractRedManipulator = new MainpulationHandler() {
        @Override
        public void manipulateImage(Bitmap bitmap, int x, int y, int pixel) {
            bitmap.setPixel(x,y,Color.argb(getAlpha(pixel),0,getGreen(pixel),getBlue(pixel)));
        }
    };
    private static MainpulationHandler sepiaFilterManipulator = new MainpulationHandler() {
        @Override
        public void manipulateImage(Bitmap bitmap, int x, int y, int pixel) {
            int newRedChannel = getAdditionOfAllChannels(pixel,0.393f,0.769f,0.189f);
            int newGreenChannel = getAdditionOfAllChannels(pixel,0.349f,0.686f,0.168f);
            int newBlueChannel = getAdditionOfAllChannels(pixel,0.272f,0.534f,0.131f);
            newRedChannel = exceeds255(newRedChannel);
            newGreenChannel = exceeds255(newGreenChannel);
            newBlueChannel = exceeds255(newBlueChannel);
            bitmap.setPixel(x,y,Color.argb(getAlpha(pixel),newRedChannel,newGreenChannel,newBlueChannel));
        }
    };
    private static int[][] getPixels(Bitmap bitmap) {
        int pixels[][] = new int[bitmap.getWidth()][bitmap.getHeight()];
        for(int i=0;i<bitmap.getWidth();i++) {
            for(int j=0;j<bitmap.getHeight();j++) {
                pixels[i][j] = bitmap.getPixel(i,j);
            }
        }
        return pixels;
    }
    private static int exceeds255(int value) {
        return (value>255)?255:value;
    }
    private static int getAlpha(int pixel) {
        return Color.alpha(pixel);
    }
    private static int getRed(int pixel) {
        return Color.red(pixel);
    }
    private static int getGreen(int pixel) {
        return Color.green(pixel);
    }
    private static int getBlue(int pixel) {
        return Color.blue(pixel);
    }
    private static void createNewImageWithFilter(Bitmap bitmap,ImageView imageView,MainpulationHandler manipulationHandler) {
        Bitmap newBitmap = createBitmapFromOrignalBitmap(bitmap);
        int pixels[][] = getPixels(bitmap);
        int w= bitmap.getWidth(),h = bitmap.getHeight();
        ImageMainpThread imageMainpThread = new ImageMainpThread(newBitmap,pixels,0,0,w,h,manipulationHandler,imageView);
        new Thread(imageMainpThread).start();
    }
    public static void createGrayScaleImage(Bitmap bitmap,ImageView imageView){
        createNewImageWithFilter(bitmap,imageView,grayScaleManipulator);
    }
    public static Bitmap compressBitmapToScale(Bitmap orginalBitmap,float scale) {
        Bitmap bitmap = Bitmap.createBitmap((int)(orginalBitmap.getWidth()*scale),(int)(orginalBitmap.getHeight()*scale),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.save();
        canvas.translate(bitmap.getWidth()/2,bitmap.getHeight()/2);
        canvas.scale(scale,scale);
        canvas.drawBitmap(orginalBitmap,-orginalBitmap.getWidth()/2,-orginalBitmap.getHeight()/2,paint);
        canvas.restore();
        return bitmap;
    }
    private static int getAdditionOfAllChannels(int pixel,float rValue,float gValue,float bValue ) {
        int red = getRed(pixel),green = getGreen(pixel),blue = getBlue(pixel);
        return (int)(red*rValue+green*gValue+blue*bValue);
    }
    public static void subtractRedChannelFromBitmap(Bitmap bitmap,ImageView imageView) {
        createNewImageWithFilter(bitmap,imageView,subtractRedManipulator);
    }
    public static void applySepiaFilter(Bitmap bitmap,ImageView imageView) {
        createNewImageWithFilter(bitmap,imageView,sepiaFilterManipulator);
    }
    private static Bitmap createBitmapFromOrignalBitmap(Bitmap bitmap) {
        return Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    }
    private static class ImageMainpThread implements Runnable{
        private int startI,startJ,endI,endJ;
        private Bitmap bitmap;
        private MainpulationHandler mainpulationHandler;
        private ImageView imageView;
        private int pixels[][];
        public ImageMainpThread(Bitmap bitmap, int pixels[][], int startI, int startJ, int endI, int endJ, MainpulationHandler mainpulationHandler, ImageView imageView) {
            this.startI = startI;
            this.startJ = startJ;
            this.endI = endI;
            this.endJ = endJ;
            this.bitmap = bitmap;
            this.pixels = pixels;
            this.mainpulationHandler = mainpulationHandler;
            this.imageView = imageView;
        }
        public void run() {
            for(int i=startI;i<endI;i++) {
                for(int j=startJ;j<endJ;j++) {
                    mainpulationHandler.manipulateImage(bitmap,i,j,pixels[i][j]);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                       imageView.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }
    private static interface MainpulationHandler {
         void manipulateImage(Bitmap bitmap,int x,int y,int pixel);
    }
}
