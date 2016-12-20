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

    private static int[][] getPixels(Bitmap bitmap) {
        int pixels[][] = new int[bitmap.getWidth()][bitmap.getHeight()];
        for(int i=0;i<bitmap.getWidth();i++) {
            for(int j=0;j<bitmap.getHeight();j++) {
                pixels[i][j] = bitmap.getPixel(i,j);
            }
        }
        return pixels;
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
    public static void createGrayScaleImage(Bitmap bitmap,ImageView imageView){
        Bitmap newBitmap = createBitmapFromOrignalBitmap(bitmap);
        int pixels[][] = getPixels(bitmap);
        int w= bitmap.getWidth(),h = bitmap.getHeight();
        ImageMainpThread imageMainpThread = new ImageMainpThread(newBitmap,pixels,0,0,w,h,grayScaleManipulator,imageView);
        new Thread(imageMainpThread).start();

    }
    public static void subtractRedChannelFromBitmap(Bitmap bitmap,ImageView imageView) {
        Bitmap newBitmap = createBitmapFromOrignalBitmap(bitmap);
        int pixels[][] = getPixels(bitmap);
        int w= bitmap.getWidth(),h = bitmap.getHeight();
        ImageMainpThread imageMainpThread = new ImageMainpThread(newBitmap,pixels,0,0,w,h,subtractRedManipulator,imageView);
        new Thread(imageMainpThread).start();
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
