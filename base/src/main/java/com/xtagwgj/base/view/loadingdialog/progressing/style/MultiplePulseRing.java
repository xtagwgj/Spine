package com.xtagwgj.base.view.loadingdialog.progressing.style;


import com.xtagwgj.base.view.loadingdialog.progressing.sprite.Sprite;
import com.xtagwgj.base.view.loadingdialog.progressing.sprite.SpriteContainer;

/**
 * Created by ybq.
 */
public class MultiplePulseRing extends SpriteContainer {

    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new PulseRing(),
                new PulseRing(),
                new PulseRing(),
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].setAnimationDelay(200 * (i + 1));
        }
    }
}
