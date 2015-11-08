package bitmap.trainning.google.com.displaybitmapsefficiently.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import bitmap.trainning.google.com.displaybitmapsefficiently.R;
import bitmap.trainning.google.com.displaybitmapsefficiently.util.core.ImageDownloader;

public class MainActivity extends Activity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.mImageView);

       /* // 界面渲染结束后设置进去, 可获取高度
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 视图树, 界面渲染结束时被调用
                String s = MoreObjects.toStringHelper(this)
                        .add("width", mImageView.getWidth())
                        .add("height", mImageView.getHeight())
                        .toString();
                Log.i("cz", s);
                BitmapScaleUtils.loadBitmap(getBaseContext(), R.mipmap.lxs, mImageView);
                mImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });*/

        /**
         * example url :
         * http://e.hiphotos.baidu.com/image/pic/item/d788d43f8794a4c201bd81230cf41bd5ad6e391f.jpg
         * http://c.hiphotos.baidu.com/image/h%3D200/sign=08523c111fd5ad6eb5f963eab1ca39a3/377adab44aed2e73519d81f98301a18b86d6faeb.jpg
         * http://g.hiphotos.baidu.com/image/h%3D360/sign=f0187d289d510fb367197191e932c893/b999a9014c086e060b400cda01087bf40bd1cbb2.jpg
         */
        ImageDownloader.display(this,"http://g.hiphotos.baidu.com/image/h%3D360/sign=f0187d289d510fb367197191e932c893/b999a9014c086e060b400cda01087bf40bd1cbb2.jpg", mImageView);
    }
}
