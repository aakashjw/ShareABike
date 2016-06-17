package activity;

/**
 * Created by aakash on 4/6/16.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aakash.materialdesign.MapsActivity;
import com.example.aakash.materialdesign.R;


public class MessagesFragment extends Fragment {

    static View rootView=null;
    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        // Inflate the layout for this fragment
        MessagesFragment.setTextView();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static void setTextView() {
        MapsActivity.getClients();

    }
    public static void changeText(String result){
        Log.i("maps", "oncreate of message fragment" + result);
        TextView tv=(TextView)rootView.findViewById(R.id.textview);
        tv.setText(result);
    }
}