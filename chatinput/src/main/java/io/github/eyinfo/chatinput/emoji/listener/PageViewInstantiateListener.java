package io.github.eyinfo.chatinput.emoji.listener;

import android.view.View;
import android.view.ViewGroup;

import io.github.eyinfo.chatinput.emoji.data.PageEntity;

public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
