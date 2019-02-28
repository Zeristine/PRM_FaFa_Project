package day01.huy.imagechoosing.CelebNameAPI;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchCelebName extends AsyncTask<String,Void,String> {


    private WeakReference<TextView> nameText;
    private WeakReference<TextView> descText;

    public FetchCelebName(TextView nameText, TextView descText){
        this.nameText = new WeakReference<>(nameText);
        this.descText = new WeakReference<>(descText);
    }

    @Override
    protected String doInBackground(String... strings) {
        return APIUtils.getCelebInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);

        try{

            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("query");

            String name = null;
            String des = null;

            if (name == null && des == null){

                JSONArray page = itemsArray.getJSONArray(0);
                JSONObject detail = page.getJSONObject(0);

                try{

                    name = detail.getString("title");
                    des = detail.getString("extract");

                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            if(name != null && des != null){
                nameText.get().setText(name);
                descText.get().setText(des);
            }else {
                nameText.get().setText("No Result Found");
                descText.get().setText("");
            }

        }catch (Exception e){
            nameText.get().setText("No Result Found");
            descText.get().setText("");
            e.printStackTrace();
        }
    }
}
