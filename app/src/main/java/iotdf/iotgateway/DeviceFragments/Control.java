package iotdf.iotgateway.DeviceFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iotdf.iotgateway.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Control.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Control#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Control extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, null);
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}