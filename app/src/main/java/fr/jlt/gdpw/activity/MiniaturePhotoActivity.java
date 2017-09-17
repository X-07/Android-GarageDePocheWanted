package fr.jlt.gdpw.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.imagezoom.ImageAttacher.OnPhotoTapListener;

import fr.jlt.gdpw.R;
import fr.jlt.gdpw.metier.MiniatureCste;


/**
 * Created by jluc1404x on 18/07/15.
 */
public class MiniaturePhotoActivity extends Activity {
    ImageView mImaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miniature_photo);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mImaView = (ImageView) findViewById(R.id.miniaturePhoto);

        String imageName = this.getIntent().getExtras().getString("idPhoto");
        this.getIntent().removeExtra("idPhoto");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        //Bitmap bimtBitmap = BitmapFactory.decodeResource(getResources(), idPhoto, options);
        Bitmap bimtBitmap = BitmapFactory.decodeFile(imageName);
        mImaView.setImageBitmap(bimtBitmap);
        usingSimpleImage(mImaView);

    }

    public void usingSimpleImage(ImageView imageView) {
        ImageAttacher mAttacher = new ImageAttacher(imageView);
        ImageAttacher.MAX_ZOOM = 2.0f; // Double the current Size
        ImageAttacher.MIN_ZOOM = 0.5f; // Half the current Size
        MatrixChangeListener mMaListener = new MatrixChangeListener();
        mAttacher.setOnMatrixChangeListener(mMaListener);
        PhotoTapListener mPhotoTap = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(mPhotoTap);
    }

    // termine l'activité (donc retour à l'activité précédente) si on clique sur la vue
    private class PhotoTapListener implements OnPhotoTapListener {
        @Override
        public void onPhotoTap(View view, float x, float y) {
            // On termine cette activité
            finish();
        }
    }

    private class MatrixChangeListener implements OnMatrixChangedListener {
        @Override
        public void onMatrixChanged(RectF rect) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Désactive la touche retour
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // On termine cette activité
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
