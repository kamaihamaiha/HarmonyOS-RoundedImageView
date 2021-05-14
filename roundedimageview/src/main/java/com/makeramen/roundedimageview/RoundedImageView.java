/*
 * Copyright (C) 2017 Vincent Mi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.makeramen.roundedimageview;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.render.Shader;
import ohos.agp.render.Canvas;
import ohos.app.Context;
import ohos.agp.utils.Color;
import ohos.agp.components.element.Element;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.Resource;
import ohos.agp.components.Image;
import ohos.agp.components.AttrSet;
import ohos.media.image.PixelMap;
import ohos.agp.components.element.ShapeElement;
import ohos.media.image.common.AlphaType;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;


public class RoundedImageView extends Image {
    private Context context;
    // Constants for tile mode attributes
    public static final int LIGHT_GREEN = 0xFF00FF00;
    private static final int TILE_MODE_UNDEFINED = -2;
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_MIRROR = 2;
    private static final String ATTRIBUTE_BORDER_COLOR = "riv_border_color";

    private static final String ATTRIBUTE_BORDER_WIDTH = "riv_border_width";

    private static final String ATTRIBUTE_OVAL = "riv_oval";

    public static final String TAG = RoundedImageView.class.getSimpleName();
    public static final float DEFAULT_RADIUS = 0f;
    public static final float DEFAULT_BORDER_WIDTH = 0f;
    public static final Shader.TileMode DEFAULT_TILE_MODE = Shader.TileMode.CLAMP_TILEMODE;
    private static final ScaleMode[] SCALE_TYPES = {
            ScaleMode.ZOOM_CENTER,
            ScaleMode.ZOOM_START,
            ScaleMode.ZOOM_END,
            ScaleMode.STRETCH,
            ScaleMode.CENTER,
            ScaleMode.INSIDE
    };

    private final float[] mCornerRadii = new float[] { DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_RADIUS };

    private Element mBackgroundDrawable;
    private Color mBorderColor =RoundedDrawable.DEFAULT_BORDER_COLOR;
    private float mBorderWidth = DEFAULT_BORDER_WIDTH;
    private PixelMap mpixelmap;
    private boolean mColorMod = false;
    private Element mElement;
    private boolean mHasColorFilter = false;
    private boolean mIsOval = false;
    private boolean mMutateBackground = false;
    private int mResource;
    private int mBackgroundResource;
    private ScaleMode mScaleType;
    private Shader.TileMode mTileModeX = DEFAULT_TILE_MODE;
    private Shader.TileMode mTileModeY = DEFAULT_TILE_MODE;

    public RoundedImageView(Context context) {

        super(context);
        init(context);
    }

    public RoundedImageView(Context context, AttrSet attrs) {
        this(context, attrs, null);
    }

    public RoundedImageView(Context context, AttrSet attrs, String defStyle) {
        super(context, attrs, defStyle);

        if (attrs.getAttr(ATTRIBUTE_BORDER_COLOR).isPresent()) {
            mBorderColor = attrs.getAttr(ATTRIBUTE_BORDER_COLOR).get().getColorValue();
        }

        if (attrs.getAttr(ATTRIBUTE_BORDER_WIDTH).isPresent()) {
            mBorderWidth = attrs.getAttr(ATTRIBUTE_BORDER_WIDTH).get().getDimensionValue();
        }

        if ( attrs.getAttr(ATTRIBUTE_OVAL).isPresent()) {
            mIsOval = attrs.getAttr(ATTRIBUTE_OVAL).get().getBoolValue();
        }

        init(context);
    }

    private void init(Context context) {
        this.context =context;
        setScaleMode(ScaleMode.ZOOM_CENTER);

        mCornerRadii[Corner.TOP_LEFT] =0;
        mCornerRadii[Corner.TOP_RIGHT] =0;
        mCornerRadii[Corner.BOTTOM_RIGHT] =0;
        mCornerRadii[Corner.BOTTOM_LEFT] =0;

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(true);

        if (mMutateBackground) {
            super.setBackground(mBackgroundDrawable);
        }

        setLayoutRefreshedListener(new RefreshListener());

        this.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component compoent, Canvas canvas) {

                if(null != mElement) {
                    mElement.drawToCanvas(canvas);
                }
            }
        });
    }

    /**
     * RefreshListener updates the view
     */
    class RefreshListener implements LayoutRefreshedListener {
        @Override
        public void onRefreshed(Component component) {
            if (mElement == null) {
                return;
            }

            if (mElement instanceof RoundedDrawable) {
                ((RoundedDrawable) mElement).updateShaderMatrix();
            }
            invalidate();
        }
    }


    private static Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case TILE_MODE_CLAMP:
                return Shader.TileMode.CLAMP_TILEMODE;
            case TILE_MODE_REPEAT:
                return Shader.TileMode.REPEAT_TILEMODE;
            case TILE_MODE_MIRROR:
                return Shader.TileMode.MIRROR_TILEMODE;
            default:
                return null;
        }
    }

  @Override
  public ScaleMode getScaleMode() {
    return mScaleType;
  }


  @Override
  public void setScaleMode(Image.ScaleMode scaleType) {
        assert scaleType != null;
        LogUtil.info(TAG, "setScaleMode and scaleType "+ scaleType);
        if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CLIP_CENTER:
                case INSIDE:
                case ZOOM_CENTER:
                case ZOOM_END:
                case ZOOM_START:
                case STRETCH:
                    super.setScaleMode(ScaleMode.STRETCH);
                    break;
                default:
                    super.setScaleMode(scaleType);
                    break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }
    }

    public void setForeground(Element drawable) {
        mResource = 0;
        mElement = RoundedDrawable.fromElement(this, drawable, context, mResource);
        updateDrawableAttrs();
        super.setForeground(mElement);
    }

    public  ShapeElement makeElement(int resvalue) {
        int color =ResUtil.getColor(mContext, resvalue);
        ShapeElement drawable = new ShapeElement();
        drawable.setShape(ShapeElement.RECTANGLE);
        drawable.setRgbColor(RgbColor.fromArgbInt(color));
        return drawable;
    }

    public void setColorResource(int resId)  {
        if (mResource != resId) {
            mResource = resId;
            mElement = (Element)makeElement(resId);

            mElement = (Element)RoundedDrawable.fromElement(this, mElement, context, mResource);
        }
    }

    public void setImageResource(int resId)  {

        if (mResource != resId) {
            mResource = resId;
            mElement = resolveResource();
            super.setPixelMap( createTransparentPixelMap(mElement));
            RoundedDrawable.fromElement(this, mElement, context, mResource);
        }
    }

    /**
     * Creates an empty pixel map
     *
     * @param element
     * @return pixelMap
     */
    private PixelMap createTransparentPixelMap(Element element) {
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.size = new Size( element.getWidth(), element.getHeight());
        initializationOptions.alphaType = AlphaType.UNKNOWN;
        initializationOptions.pixelFormat = PixelFormat.ARGB_8888;
        return PixelMap.create(initializationOptions);
    }

    public void setImageResource3(int resId)  {
        ShapeElement element = new ShapeElement();
        element.setShape(ShapeElement.RECTANGLE);
        //element.setColor(LIGHT_GREEN);

        setBackground(element);
        setVisibility(VISIBLE);
    }


    public void setImageURI(String  uri) {

        setForeground(getBackgroundElement());
    }

    private Element resolveResource() {

    ResourceManager rsrc = getContext().getResourceManager();
    if (rsrc == null) { return null; }

    Element d = null;
    if (mResource != 0) {
      try {
        Resource resource = rsrc.getResource(mResource);
        d = (Element)util.prepareElement( resource);
      } catch (Exception e) {
        LogUtil.info(TAG, "Unable to find resource: " + mResource + e);
        mResource = 0;
      }
    }
    return RoundedDrawable.fromElement(this, d, context, mResource);

    }

    private PixelMap resolveResource2() {

        ResourceManager rsrc = getContext().getResourceManager();
        if (rsrc == null) { return null; }

        PixelMap d = null;

        if (mResource != 0) {
            try {
                Resource resource = rsrc.getResource(mResource);
                d = (PixelMap)util.preparePixelmap( resource);
            } catch (Exception e) {
                LogUtil.info(TAG, "Unable to find resource: " + mResource + e);
                mResource = 0;
            }
        }
        return d;
    }

    @Override
    public void setBackground(Element background) {
        setBackgroundDrawable(background);
    }

    public void setBackgroundResource(int resId) {
        if (mBackgroundResource != resId) {
            mBackgroundResource = resId;
            mBackgroundDrawable = resolveBackgroundResource();
            setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    public void setBackgroundColor(int color) {
        setBackgroundDrawable(mBackgroundDrawable);
    }

    private Element resolveBackgroundResource() {
        ResourceManager rsrc = getContext().getResourceManager();
        if (rsrc == null) { return null; }

        Element d = null;
        if (mBackgroundResource != 0) {
            try {
                Resource resource = rsrc.getResource(mBackgroundResource);
                d = (Element)util.prepareElement( resource);
            } catch (Exception e) {
                LogUtil.info(TAG, "Unable to find resource: " + mBackgroundResource + e);
                mBackgroundResource = 0;
            }
        }

        return RoundedDrawable.fromElement(this, d, context, mBackgroundResource);
    }

    private void updateDrawableAttrs() {
        updateAttrs(mElement, mScaleType);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {

    }

    private void applyColorMod() {

    }
    private void updateAttrs(Element drawable, ScaleMode scaleType) {
        if (drawable == null) { return; }

        if (drawable instanceof RoundedDrawable) {

            ((RoundedDrawable) drawable)
                    .setScaleMode(scaleType)
                    .setBorderWidth(mBorderWidth)
                    .setBorderColor(mBorderColor)
                    .setOval(mIsOval)
                    .setTileModeX(mTileModeX)
                    .setTileModeY(mTileModeY);

            if (mCornerRadii != null) {
                ((RoundedDrawable) drawable).setCornerRadius(
                        mCornerRadii[Corner.TOP_LEFT],
                        mCornerRadii[Corner.TOP_RIGHT],
                        mCornerRadii[Corner.BOTTOM_RIGHT],
                        mCornerRadii[Corner.BOTTOM_LEFT]);
            }

            applyColorMod();
        }
    }

    @Deprecated
    public void setBackgroundDrawable(Element background) {
        mBackgroundDrawable = background;
        updateBackgroundDrawableAttrs(true);
        super.setBackground(mBackgroundDrawable);
    }

    /**
     * Function returns the value for corner, how much it will get rounded.
     * More value, more rounding effect.
     *
     * @return the largest corner radius.v
     */
    public float getCornerRadius() {
        return getMaxCornerRadius();
    }

    /**
     * Function returns the max value for corner, beyond this value corner can't get rounded.
     *
     * @return the largest corner radius.
     */
    public float getMaxCornerRadius() {
        float maxRadius = 0;
        for (float r : mCornerRadii) {
            maxRadius = Math.max(r, maxRadius);
        }
        return maxRadius;
    }

    /**
     * Get the corner radius of a specified corner.
     *
     * @param corner the corner.
     * @return the radius.
     */
    public float getCornerRadius(@Corner int corner) {
        return mCornerRadii[corner];
    }

    /**
     * Set all the corner radii from a dimension resource id.
     *
     * @param resId dimension resource id of radii.
     */
    public void setCornerRadiusDimen( int resId) {
        // convert resource to dimen
        //float radius = getResources().getDimension(resId);
        float radius = 0;
        setCornerRadius(radius, radius, radius, radius);
    }

    /**
     * Set the corner radius of a specific corner from a dimension resource id.
     *
     * @param corner the corner to set.
     * @param resId the dimension resource id of the corner radius.
     */
    public void setCornerRadiusDimen(@Corner int corner, int resId) {
        // convert resource to dimen
        //setCornerRadius(corner, getResources().getDimensionPixelSize(resId));
    }

    /**
     * Set the corner radii of all corners in px.
     *
     * @param radius the radius to set.
     */
    public void setCornerRadius(float radius) {
        setCornerRadius(radius, radius, radius, radius);
    }

    /**
     * Set the corner radius of a specific corner in px.
     *
     * @param corner the corner to set.
     * @param radius the corner radius to set in px.
     */
    public void setCornerRadius(@Corner int corner, float radius) {
        if (mCornerRadii[corner] == radius) {
            return;
        }
        mCornerRadii[corner] = radius;

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    /**
     * Set the corner radii of each corner individually. Currently only one unique nonzero value is
     * supported.
     *
     * @param topLeft radius of the top left corner in px.
     * @param topRight radius of the top right corner in px.
     * @param bottomRight radius of the bottom right corner in px.
     * @param bottomLeft radius of the bottom left corner in px.
     */
    public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (mCornerRadii[Corner.TOP_LEFT] == topLeft
                && mCornerRadii[Corner.TOP_RIGHT] == topRight
                && mCornerRadii[Corner.BOTTOM_RIGHT] == bottomRight
                && mCornerRadii[Corner.BOTTOM_LEFT] == bottomLeft) {
            return;
        }

        mCornerRadii[Corner.TOP_LEFT] = topLeft;
        mCornerRadii[Corner.TOP_RIGHT] = topRight;
        mCornerRadii[Corner.BOTTOM_LEFT] = bottomLeft;
        mCornerRadii[Corner.BOTTOM_RIGHT] = bottomRight;

        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }


    public void setBorderWidth(float width) {
        if (mBorderWidth == width) { return; }

        mBorderWidth = width;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public int getBorderColor() {
        return 0;
    }

  public Color getBorderColors() {
    return mBorderColor;
  }

  public void setBorderColor(Color colors) {
    if (mBorderColor.equals(colors)) { return; }

    mBorderColor = colors;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs(false);
    if (mBorderWidth > 0) {
      invalidate();
    }
  }

  /**
   * Return true if this view should be oval and always set corner radii to half the height or
   * width.
   *
   * @return if this {@link RoundedImageView} is set to oval.
   */
    public boolean isOval() {
        return mIsOval;
    }

    public void setOval(boolean oval) {
        mIsOval = oval;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public Shader.TileMode getTileModeX() {
        return mTileModeX;
    }

    public void setTileModeX(Shader.TileMode tileModeX) {
        if (this.mTileModeX == tileModeX) { return; }

        this.mTileModeX = tileModeX;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    public Shader.TileMode getTileModeY() {
        return mTileModeY;
    }

    public void setTileModeY(Shader.TileMode tileModeY) {
        if (this.mTileModeY == tileModeY) { return; }

        this.mTileModeY = tileModeY;
        updateDrawableAttrs();
        updateBackgroundDrawableAttrs(false);
        invalidate();
    }

    /**
     * If {@code true}, we will also round the background drawable according to the settings on this
     * ImageView.
     *
     * @return whether the background is mutated.
     */
    public boolean mutatesBackground() {
        return mMutateBackground;
    }

  /**
   * Set whether the {@link RoundedImageView} should round the background drawable according to
   * the settings in addition to the source drawable.
   *
   * @param mutate true if this view should mutate the background drawable.
   */
  public void mutateBackground(boolean mutate) {
    if (mMutateBackground == mutate) { return; }

        mMutateBackground = mutate;
        updateBackgroundDrawableAttrs(true);
        invalidate();
    }
}