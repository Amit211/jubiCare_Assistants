package com.indev.jubicare_assistants.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.indev.jubicare_assistants.HomeActivity;
import com.indev.jubicare_assistants.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupAgentFragment extends Fragment {
    @BindView(R.id.btn_sign_up)
    Button btn_sign_up;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_signup, container, false);
        ButterKnife.bind(this, v);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("FromAgent","1") ;
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }
}
