package iotdf.iotgateway.DeviceFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import iotdf.iotgateway.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Chart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Chart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chart extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, null);
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}