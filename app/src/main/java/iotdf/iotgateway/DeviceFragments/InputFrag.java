package iotdf.iotgateway.DeviceFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import iotdf.iotgateway.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 */
public class InputFrag extends android.support.v4.app.DialogFragment {


    private EditText inputValue;

    public interface inputListener{
        void onInputComplete(String inputValue);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_input, null);
        inputValue = (EditText) view.findViewById(R.id.id_ED_inputvalue);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                inputListener listener = (inputListener) getActivity();
                                listener.onInputComplete(inputValue.getText().toString());
                            }
                        })
                .setNegativeButton("取消", null);
        return builder.create();
    }
}
