package org.uiflow.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

/**
 * Keeps track of a key combination
 */
public class HotKey {

    private Array<Integer> allOf = new Array<Integer>(3);
    private Array<Integer> oneOf = new Array<Integer>(3);

    private boolean control = false;
    private boolean shift = false;
    private boolean alt = false;
    private boolean meta = false;

    public static HotKey control(int key) {
        return new HotKey(key).andControl();
    }

    public static HotKey shift(int key) {
        return new HotKey(key).andShift();
    }

    public static HotKey alt(int key) {
        return new HotKey(key).andAlt();
    }

    public static HotKey meta(int key) {
        return new HotKey(key).andMeta();
    }

    public HotKey(int required) {
        allOf.add(required);
    }

    public HotKey(int required1, int required2) {
        allOf.add(required1);
        allOf.add(required2);
    }

    public HotKey(int required, int oneOf1, int oneOf2) {
        allOf.add(required);
        oneOf.add(oneOf1);
        oneOf.add(oneOf2);
    }

    public HotKey(Array<Integer> allOf, Array<Integer> oneOf) {
        this.allOf = allOf;
        this.oneOf = oneOf;
    }

    public HotKey require(int keyCode) {
        allOf.add(keyCode);
        return this;
    }

    public HotKey oneOf(int keyCode) {
        oneOf.add(keyCode);
        return this;
    }

    public HotKey andAlt() {
        alt = true;
        return this;
    }

    public HotKey andControl() {
        control = true;
        return this;
    }

    public HotKey andShift() {
        shift = true;
        return this;
    }

    public HotKey andMeta() {
        meta = true;
        return this;
    }

    public Array<Integer> getAllOf() {
        return allOf;
    }

    public Array<Integer> getOneOf() {
        return oneOf;
    }
}
