package pl.schibsted.chat.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import pl.schibsted.chat.App;
import pl.schibsted.chat.R;
import pl.schibsted.chat.utils.SimpleLog;

import java.io.InputStream;

/**
 * @author krzysztof.kosobudzki
 */
public class DownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private final ImageView mImageView;
    private String mUrl;

    public DownloadAsyncTask(ImageView imageView) {
//        mImageView = new WeakReference(imageView);
        mImageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        mUrl = params[0];

        if (App.getInstance().getImage(mUrl) != null) {
            return App.getInstance().getImage(mUrl);
        }

        return downloadBitmap(params[0]);
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (mImageView != null) {
                if (bitmap != null) {
                    mImageView.setImageBitmap(bitmap);

                    App.getInstance().putImage(mUrl, bitmap);
                } else {
                    mImageView.setImageDrawable(mImageView.getContext().getResources().getDrawable(R.drawable.no_photo));
                }
        }
    }

    private static Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                SimpleLog.d("Download image error ", statusCode);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;

                try {
                    inputStream = entity.getContent();

                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            SimpleLog.d("Download image error while retrieving bitmap from ", url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
}
