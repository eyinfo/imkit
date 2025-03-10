package io.github.eyinfo.chatinput.listener;


import java.util.List;

import io.github.eyinfo.chatinput.model.FileItem;

/**
 * Menu items' callbacks
 */
public interface OnMenuClickListener {

    /**
     * Fires when send button is on click.
     *
     * @param input Input content
     * @return boolean
     */
    boolean onSendTextMessage(CharSequence input);

    /**
     * Fires when voice button is on click.
     */
    boolean switchToMicrophoneMode();

    /**
     * Fires when photo button is on click.
     */
    boolean switchToGalleryMode();

    /**
     * Files when send photos or videos.
     * When construct send message, you need to judge the type
     * of file item, according to
     *
     * @param list List of file item objects
     */
    void onSendFiles(List<FileItem> list);

    /**
     * Fires when camera button is on click.
     */
    boolean switchToCameraMode();

    /**
     * Fires when emoji button is on click.
     */
    boolean switchToEmojiMode();
}