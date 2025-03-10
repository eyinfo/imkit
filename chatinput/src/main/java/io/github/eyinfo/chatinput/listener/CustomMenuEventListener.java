package io.github.eyinfo.chatinput.listener;


import io.github.eyinfo.chatinput.menu.view.MenuFeature;
import io.github.eyinfo.chatinput.menu.view.MenuItem;

/**
 * Custom Menu' callbacks
 */
public interface CustomMenuEventListener {

    boolean onMenuItemClick(String tag, MenuItem menuItem);

    void onMenuFeatureVisibilityChanged(int visibility, String tag, MenuFeature menuFeature);

}