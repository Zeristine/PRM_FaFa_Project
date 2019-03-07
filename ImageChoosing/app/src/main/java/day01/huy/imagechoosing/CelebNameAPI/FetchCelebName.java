package day01.huy.imagechoosing.CelebNameAPI;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Iterator;

public class FetchCelebName extends AsyncTask<String, Void, String> {


    private TextView nameText;
    private TextView descText;

    public FetchCelebName(TextView nameText, TextView descText) {
        this.nameText = nameText;
        this.descText = descText;
    }

    @Override
    protected String doInBackground(String... strings) {
        return APIUtils.getCelebInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            System.out.println("==================================result: " + s);

            JSONObject rootJson = new JSONObject(s);
            JSONObject query = rootJson.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");
            Iterator<String> it = pages.keys();

            String name = null;
            String des = null;
            if (it.hasNext()) {
                String dynamicIdString = it.next();
                JSONObject dynamicId = new JSONObject(pages.getJSONObject(dynamicIdString).toString());
                name = dynamicId.getString("title");
                des = dynamicId.getString("extract");
            }


            if (name != null && des != null) {
                nameText.setText(name);
                descText.setText(des);
            } else {
                nameText.setText("No Resulttt Found");
                descText.setText("");
            }

        } catch (Exception e) {
            nameText.setText("Noooo Result Found");
            descText.setText("");
            e.printStackTrace();
        }
    }
}
