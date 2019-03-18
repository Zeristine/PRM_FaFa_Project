package day01.huy.imagechoosing.CelebNameAPI;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Iterator;

import day01.huy.imagechoosing.fileProcess.InfoGetter;

public class FetchCelebName extends AsyncTask<String, Void, String> {


    private TextView nameText;
    private TextView descText;
    private String imgUrl;
    public InterfaceFetchCelebResponse infoActivity;

    public FetchCelebName(TextView nameText, TextView descText, String imgUrl) {
        this.nameText = nameText;
        this.descText = descText;
        this.imgUrl = imgUrl;
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
                FetchCelebResponse fcr = new FetchCelebResponse(name, des, this.imgUrl);
                infoActivity.onFetchCelebResponse(fcr);
//                InfoGetter infoGetter = new InfoGetter();
//                infoGetter.setDescription(des);
//                infoGetter.setName(name);
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
