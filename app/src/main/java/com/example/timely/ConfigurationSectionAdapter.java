package com.example.timely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import dataStructures.Section;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ConfigurationSectionAdapter extends RecyclerView.Adapter implements DragHelperAdapter {

    private ArrayList<Section> list;

    private final PublishSubject<Section> onClickEvent = PublishSubject.create();
    private final PublishSubject<Boolean> onMoveEvent = PublishSubject.create();

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        onMoveEvent.onNext(true);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ConfigurationSectionViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public ConfigurationSectionViewHolder(LinearLayout l) {
            super(l);
            layout = l;
        }
    }

    public ConfigurationSectionAdapter(ArrayList<Section> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ConfigurationSectionAdapter.ConfigurationSectionViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_configuration_section_item_view, parent, false);
        ConfigurationSectionAdapter.ConfigurationSectionViewHolder vh = new ConfigurationSectionAdapter.ConfigurationSectionViewHolder(l);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LinearLayout view = ((ConfigurationSectionAdapter.ConfigurationSectionViewHolder)holder).layout;

        final Section currentSection = list.get(position);
        TextView section_name= view.findViewById(R.id.section_name);
        section_name.setText(currentSection.sectionName);
        TextView duration= view.findViewById(R.id.duration);
        duration.setText(currentSection.getDurationString());

        //Handle buttons and add onClickListeners
        ImageButton callbtn = view.findViewById(R.id.Edit);

        callbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                onClickEvent.onNext(currentSection);
            }
        });
    }

    public Observable<Section> onEditClicked() {
        return onClickEvent.hide();
    }
    public Observable<Boolean> onReorder() {
        return onMoveEvent.hide();
    }
}