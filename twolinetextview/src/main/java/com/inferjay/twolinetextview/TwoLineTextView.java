/**
 * Copyright 2014 Inferjay (HC ZHANG) http://www.inferjay.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inferjay.twolinetextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 *
 * <p>Description: 可以显示2行文本的 TextView </p>
 * <p>Copyright: Copyright (c) 2015</p>
 *
 * @since 2015-6-5 下午1:27:19
 * @author inferjay
 * @version 1.0
 */
public class TwoLineTextView extends TextView {
    private static final String LINE_BR = "\n";
    private static final int MAX_LINEs = 2;
    private CharSequence mFirstLineText; // TODO: use a default from R.string...
    private CharSequence mSecondLineText;
    private int mFirstLineTextColor = Color.BLACK; // TODO: use a default from R.color...
    private int mDefaultTextSize = 14;
    private float mFirstLineTextSize; // TODO: use a default from R.dimen...
    private float mLineSpaceHeight = 14; // no implement

    private SpannableStringBuilder textSpannableBuilder;

    private int firstLineTextSpanEnd;

    public TwoLineTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public TwoLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TwoLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        setMaxLines(MAX_LINEs);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TwoLineTextView, defStyle, 0);

        mFirstLineText = a.getString(
                R.styleable.TwoLineTextView_firstLineText);
        mFirstLineTextColor = a.getColor(
                R.styleable.TwoLineTextView_firstLineTextColor,
                mFirstLineTextColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mFirstLineTextSize = a.getDimension(
                R.styleable.TwoLineTextView_firstLineTextSize,
                mDefaultTextSize);
        mLineSpaceHeight = a.getDimension(
                R.styleable.TwoLineTextView_lineSpaceHeight,
                mLineSpaceHeight);

        a.recycle();

        mSecondLineText = getText();
        if (!TextUtils.isEmpty(mFirstLineText)) {
            initFirstSpanEnd();
            initTextSpannableBuilder();
            handleFirstLineTextStyle();
            setText(textSpannableBuilder, BufferType.SPANNABLE);
        }
    }

    private void initTextSpannableBuilder() {
        initTextSpannableBuilder(mFirstLineText, mSecondLineText);
    }

    private void initTextSpannableBuilder(CharSequence firstLineText, CharSequence secondLineText) {
        textSpannableBuilder = new SpannableStringBuilder(firstLineText + LINE_BR + secondLineText);
    }

    private void initFirstSpanEnd() {
        firstLineTextSpanEnd = TextUtils.isEmpty(getFirstLineText()) ? 0 : getFirstLineText().length();
    }


    /**
     * Gets the first line text string attribute value.
     *
     * @return The first line text string attribute value.
     */
    public CharSequence getFirstLineText() {
        return TextUtils.isEmpty(mFirstLineText) ? "" : mFirstLineText;
    }

    /**
     * Sets the view's first line text string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param firstText The firstText attribute value to use.
     */
    public void setFirstLineText(CharSequence firstText) {
        mFirstLineText = firstText;
        initFirstSpanEnd();
        initTextSpannableBuilder();
        handleFirstLineTextStyle();
        setText(textSpannableBuilder, BufferType.SPANNABLE);
    }

    /**
     * Gets the first line text color attribute value.
     *
     * @return The first line text color attribute value.
     */
    public int getFirstLineTextColor() {
        return mFirstLineTextColor;
    }

    /**
     * Sets the view's first line text color attribute value. In the example view, this color
     * is the font color.
     *
     * @param firstTextColor The first line text color attribute value to use.
     */
    public void setFirstLineTextColor(int firstTextColor) {
        mFirstLineTextColor = firstTextColor;
        handleFirstLineTextSpanColor();
        setText(textSpannableBuilder, BufferType.SPANNABLE);
    }

    private void handleFirstLineTextSpanColor() {
        handleTextSpanColor(new ForegroundColorSpan(getFirstLineTextColor()), 0, firstLineTextSpanEnd);
    }

    /**
     * Gets the first line text dimension attribute value.
     *
     * @return The first line text size attribute value.
     */
    public float getFirstLineTextSize() {
        return mFirstLineTextSize;
    }

    /**
     * Sets the view's first line text size attribute value. In the two line view, this dimension
     * is the font size.
     *
     * @param firstTextSize The example dimension attribute value to use.
     */
    public void setFirstLineTextSize(float firstTextSize) {
        mFirstLineTextSize = firstTextSize;
        handleFirstLineTextSizeSpan();
        setText(textSpannableBuilder, BufferType.SPANNABLE);
    }

    public float getLineSpaceHeight() {
        return mLineSpaceHeight;
    }

    public void setLineSpaceHeight(float lineSpaceHeight) {
        this.mLineSpaceHeight = lineSpaceHeight;
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        if (type != BufferType.SPANNABLE && !TextUtils.isEmpty(getFirstLineText())) {
            mSecondLineText = text;

            initTextSpannableBuilder(mFirstLineText, mSecondLineText);

            handleFirstLineTextStyle();

            handleSecondLineTextSpanColor();
            super.setText(textSpannableBuilder, BufferType.NORMAL);
        } else {
            super.setText(text, BufferType.NORMAL);
        }

    }

    private void handleSecondLineTextSpanColor() {
        int secondTextSpanStart = firstLineTextSpanEnd + LINE_BR.length();
        int secondTextSpanEnd = secondTextSpanStart +  (TextUtils.isEmpty(mSecondLineText) ? 0 : mSecondLineText.length());
        handleTextSpanColor(new ForegroundColorSpan(getCurrentTextColor()), secondTextSpanStart, secondTextSpanEnd);
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        handleSecondLineTextSpanColor();
        setText(textSpannableBuilder, BufferType.SPANNABLE);
    }


    private void handleFirstLineTextStyle() {
        handleFirstLineTextSpanColor();

        handleFirstLineTextSizeSpan();
    }

    private void handleFirstLineTextSizeSpan() {
        textSpannableBuilder.setSpan(new AbsoluteSizeSpan((int) getFirstLineTextSize()),
                0, firstLineTextSpanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void handleTextSpanColor(ForegroundColorSpan what, int start, int end) {
        if (null != textSpannableBuilder) {
            textSpannableBuilder.setSpan(what,
                    start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}
