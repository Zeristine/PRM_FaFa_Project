package day01.huy.imagechoosing.fileProcess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchAPI;
import com.microsoft.azure.cognitiveservices.search.imagesearch.BingImageSearchManager;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImageObject;
import com.microsoft.azure.cognitiveservices.search.imagesearch.models.ImagesModel;

import java.io.InputStream;

public class ImageProcess extends AsyncTask<String, Void, Bitmap>{
    ImageView bmImage;
    BingImageSearchAPI client;
    final String subKey = "40985fb2f22f430d8468b729052a148e";

    public  ImageProcess(ImageView bmImage) {
        this.bmImage = bmImage;
        client = BingImageSearchManager.authenticate(subKey);
    }
    @Override
    protected Bitmap doInBackground(String... urls) {
        String searchTerm = urls[0];
        Bitmap mIcon11 = null;
        ImagesModel imageResults = client.bingImages().search()
                .withQuery(searchTerm)
                .withMarket("en-us")
                .execute();
        String imgUrl = imageResults.value().get(0).thumbnailUrl();
        try {

            InputStream in = new java.net.URL(imgUrl).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bmImage.setImageBitmap(bitmap);
    }
}
