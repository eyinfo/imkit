package io.github.eyinfo.chatinput.emoji.data;

import android.view.View;
import android.view.ViewGroup;

import io.github.eyinfo.chatinput.emoji.listener.PageViewInstantiateListener;

/**
 * use XhsEmotionsKeyboard(<a href="https://github.com/w446108264/XhsEmoticonsKeyboard">...</a>)
 * author: sj
 */
public class PageEntity<T extends PageEntity> implements PageViewInstantiateListener<T> {

    protected View mRootView;

    protected PageViewInstantiateListener mPageViewInstantiateListener;

    public void setIPageViewInstantiateItem(PageViewInstantiateListener pageViewInstantiateListener) {
        this.mPageViewInstantiateListener = pageViewInstantiateListener;
    }

    public View getRootView() {
        return mRootView;
    }

    public void setRootView(View rootView) {
        this.mRootView = rootView;
    }

    public PageEntity() {
    }

    public PageEntity(View view) {
        this.mRootView = view;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position, T pageEntity) {
        if (mPageViewInstantiateListener != null) {
            return mPageViewInstantiateListener.instantiateItem(container, position, this);
        }
        return getRootView();
    }
}
