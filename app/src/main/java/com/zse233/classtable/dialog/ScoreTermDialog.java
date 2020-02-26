package com.zse233.classtable.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zse233.classtable.ClassTableRepo;
import com.zse233.classtable.R;
import com.zse233.classtable.misc.MiscClass;
import com.zse233.classtable.term.TermInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ScoreTermDialog extends DialogFragment {
    private ClassTableRepo repo = new ClassTableRepo();
    private List<TermInfo> terms = new ArrayList<>();
    private int termCode = 34;
    private String termName = "";
    private SharedPreferences shp;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View inflater = requireActivity().getLayoutInflater().inflate(R.layout.dialog_score_choose_term, null);
        final String userKey = MiscClass.getUserKey();
        shp = getActivity().getSharedPreferences("first_day", Context.MODE_PRIVATE);
        Toast.makeText(getContext(), "正在获取学期信息", Toast.LENGTH_LONG).show();

        final Spinner spinner = inflater.findViewById(R.id.term_list);
        final Set<String> termsName = shp.getStringSet("terms", new ArraySet<String>());
        final List<String> termsNameList = new ArrayList<>(termsName);
        termsNameList.sort(new TermCompare());
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.term_item, termsNameList);
        //TermAdapter adapter = new TermAdapter(terms, getContext());
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                termName = termsNameList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(inflater);
        builder.setPositiveButton(R.string.request_score, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                termCode = shp.getInt(termName, MiscClass.getTermCode());
                SharedPreferences.Editor editor = shp.edit();
                editor.putInt("curr_term", termCode);
                editor.apply();
            }
        });
        return builder.create();
    }


}

class TermAdapter extends BaseAdapter {
    private List<TermInfo> terms;
    private Context mContext;

    public TermAdapter(List<TermInfo> terms, Context mContext) {
        this.terms = terms;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return terms.size();
    }

    @Override
    public Object getItem(int position) {
        return terms.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.term_item, null);
        if (convertView != null) {
            TextView textView = convertView.findViewById(R.id.term_name);
            textView.setText((String) getItem(position));
        }
        return convertView;
    }
}

class TermCompare implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        int o1_start = Integer.parseInt(o1.substring(0, 4));
        int o2_start = Integer.parseInt(o2.substring(0, 4));
        if (o1_start != o2_start) {
            return o2_start - o1_start;
        } else {
            if (o1.contains("一")) {
                return 1;
            } else {
                return -1;
            }
        }

    }
}
