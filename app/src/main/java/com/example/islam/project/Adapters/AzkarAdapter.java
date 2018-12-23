package com.example.islam.project.Adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.islam.project.AzkarParam;
import com.example.islam.project.MyApplication;
import com.example.islam.project.R;

import java.util.List;
import java.util.Locale;

public class AzkarAdapter extends RecyclerView.Adapter<AzkarAdapter.AzkarViewHolder> {
    private String[] arabic;
    private String[] pronunciation;
    private String[] references;
    private String[] language;
    private AzkarParam param;
    public static class AzkarViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public AzkarViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }
    public AzkarAdapter(AzkarParam param) {
        this.param = param;
        Resources r = MyApplication.getAppContext().getResources();
        arabic = r.getStringArray(param.getArabicArrayID());
        pronunciation = r.getStringArray(param.getPronunciationArrayID());
        references = r.getStringArray(param.getReferenceArrayID());
        language = r.getStringArray(param.getLanguageArrayID());
    }

    @Override
    public AzkarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View customView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.azkar_item, parent, false);

        AzkarViewHolder vh = new AzkarViewHolder(customView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AzkarViewHolder holder, int position) {
        View customView = holder.mView;

        TextView arabicZikr = customView.findViewById(R.id.arabicZikr);
        TextView referenceTxt = customView.findViewById(R.id.referenceTxt);
        TextView pronounceZikr = customView.findViewById(R.id.pronounceZikr);
        TextView langZikr = customView.findViewById(R.id.langZikr);
        TextView numberTxt = customView.findViewById(R.id.numberTxt);

        arabicZikr.setText(arabic[position]);
        referenceTxt.setText(references[position]);
        pronounceZikr.setText(pronunciation[position]);
        langZikr.setText(language[position]);
        numberTxt.setText(String.format(Locale.US, "(%d)", position+1));
    }

    @Override
    public int getItemCount() {
        return arabic.length;
    }
}
