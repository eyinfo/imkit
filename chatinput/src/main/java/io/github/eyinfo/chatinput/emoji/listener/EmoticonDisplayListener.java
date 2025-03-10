package io.github.eyinfo.chatinput.emoji.listener;

import android.view.ViewGroup;

import io.github.eyinfo.chatinput.emoji.adapter.EmoticonsAdapter;

public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}