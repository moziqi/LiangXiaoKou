package org.liangxiaokou.module.contact;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.liangxiaokou.module.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactActivityFragment extends Fragment {

    public ContactActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }
}
