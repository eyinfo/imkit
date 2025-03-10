package io.github.eyinfo.chatinput.emoji;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;

import java.util.ArrayList;

import io.github.eyinfo.chatinput.R;
import io.github.eyinfo.chatinput.emoji.adapter.PageSetAdapter;
import io.github.eyinfo.chatinput.emoji.data.PageSetEntity;
import io.github.eyinfo.chatinput.emoji.widget.AutoHeightLayout;
import io.github.eyinfo.chatinput.emoji.widget.EmoticonsFuncView;
import io.github.eyinfo.chatinput.emoji.widget.EmoticonsIndicatorView;
import io.github.eyinfo.chatinput.emoji.widget.EmoticonsToolBarView;

public class EmojiView extends AutoHeightLayout implements EmoticonsFuncView.OnEmoticonsPageViewListener,
        EmoticonsToolBarView.OnToolBarItemClickListener {

    private EmoticonsFuncView mEmoticonsFuncView;
    private EmoticonsIndicatorView mEmoticonsIndicatorView;
    private EmoticonsToolBarView mEmoticonsToolBarView;

    public EmojiView(Context context) {
        super(context, null);
        init(context, null);
    }

    public EmojiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int i) {

    }

    public EmojiView(Context context, AttributeSet attrs,
                     @AttrRes int defStyleAttr) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_chatinput_emoji, this);

        mEmoticonsFuncView = findViewById(R.id.view_epv);
        mEmoticonsIndicatorView = findViewById(R.id.view_eiv);
        mEmoticonsToolBarView = findViewById(R.id.view_etv);
        mEmoticonsFuncView.setOnIndicatorListener(this);
        mEmoticonsToolBarView.setOnToolBarItemClickListener(this);
    }

    public void setAdapter(PageSetAdapter pageSetAdapter) {
        if (pageSetAdapter != null) {
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            if (pageSetEntities != null) {
                for (PageSetEntity pageSetEntity : pageSetEntities) {
                    mEmoticonsToolBarView.addToolItemView(pageSetEntity);
                }
            }
        }
        mEmoticonsFuncView.setAdapter(pageSetAdapter);
    }

    @Override
    public void emoticonSetChanged(PageSetEntity pageSetEntity) {
        mEmoticonsToolBarView.setToolBtnSelect(pageSetEntity.getUuid());
    }

    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playTo(position, pageSetEntity);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playBy(oldPosition, newPosition, pageSetEntity);
    }

    @Override
    public void onToolBarItemClick(PageSetEntity pageSetEntity) {
        mEmoticonsFuncView.setCurrentPageSet(pageSetEntity);
    }

    public EmoticonsFuncView getEmoticonsFuncView() {
        return this.mEmoticonsFuncView;
    }
}
