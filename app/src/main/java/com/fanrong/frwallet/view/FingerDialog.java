package com.fanrong.frwallet.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codersun.fingerprintcompat.AFingerDialog;
import com.fanrong.frwallet.R;

public class FingerDialog extends AFingerDialog implements View.OnClickListener
{

    private TextView titleTv;

    private TextView desTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_finger, null);

        TextView cancelTv = view.findViewById(R.id.tv_cancel);
        cancelTv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onSucceed()
    {
        dismiss();
    }

    @Override
    public void onFailed()
    {

    }

    @Override
    public void onHelp(String help)
    {

    }

    @Override
    public void onError(String error)
    {

    }

    @Override
    public void onCancelAuth()
    {

    }

    @Override
    public void onClick(View v)
    {
        dismiss();
    }
}



