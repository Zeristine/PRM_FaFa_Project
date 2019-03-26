package day01.huy.imagechoosing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import day01.huy.imagechoosing.Models.HistoryDTO;
import day01.huy.imagechoosing.Models.HistoryList;

public class HistoryActivity extends AppCompatActivity {

    private ListView showList;
    private TextView emptyAnnounce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        showList = findViewById(R.id.listViewHistory);
        emptyAnnounce = findViewById(R.id.txtEmpty);


        //get List and reverse
        List<HistoryDTO> historyList = HistoryList.getList();
        Collections.reverse(historyList);

        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        if(historyList.isEmpty()){
            showList.setVisibility(View.GONE);
            emptyAnnounce.setVisibility(View.VISIBLE);
        }else{
            emptyAnnounce.setVisibility(View.GONE);
            showList.setVisibility(View.VISIBLE);
            for (int i = 0; i < historyList.size(); i++) {
                Map<String, String> datum = new HashMap<String, String>();
                datum.put("1",historyList.get(i).getName());
                datum.put("2",historyList.get(i).getSearch_date());
                data.add(datum);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, data,
                    android.R.layout.simple_list_item_2,
                    new String[] {"1", "2" },
                    new int[] {android.R.id.text1, android.R.id.text2 });
            showList.setAdapter(adapter);

            //list item onClick
            showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(HistoryActivity.this, InfoActivity.class);
                    TextView name = (TextView) view.findViewById(android.R.id.text1);
                    intent.putExtra("name",name.getText().toString());
                    startActivity(intent);
                }
            });

        }

    }
}
