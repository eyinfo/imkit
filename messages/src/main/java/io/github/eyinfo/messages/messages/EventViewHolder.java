package io.github.eyinfo.messages.messages;

import android.view.View;

import io.github.eyinfo.messages.R;
import io.github.eyinfo.messages.commons.models.IMessage;
import io.github.eyinfo.messages.view.RoundTextView;

public class EventViewHolder<MESSAGE extends IMessage>
        extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private RoundTextView mEvent;

    public EventViewHolder(View itemView, boolean isSender) {
        super(itemView);
        mEvent = itemView.findViewById(R.id.aurora_tv_msgitem_event);
    }

    @Override
    public void onBind(MESSAGE message) {
        mEvent.setText(message.getText());
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        mEvent.setTextColor(style.getEventTextColor());
        mEvent.setLineSpacing(style.getEventLineSpacingExtra(), 1.0f);
        mEvent.setBgColor(style.getEventBgColor());
        mEvent.setBgCornerRadius(style.getEventBgCornerRadius());
        mEvent.setTextSize(style.getEventTextSize());
        mEvent.setPadding(style.getEventPaddingLeft(), style.getEventPaddingTop(),
                style.getEventPaddingRight(), style.getEventPaddingBottom());
    }
}
