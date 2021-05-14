/*
 * Copyright (C) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package com.makeramen.roundedimageview.slice;

import com.makeramen.roundedimageview.LogUtil;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

public class UiUtil extends ComponentContainer {
    private Context mContext;

    public UiUtil(Context context) {
        super(context);
        mContext = context;
    }


    public final ShapeElement getShapeElement(int shape, int color, float radius) {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setShape(shape);
        shapeElement.setRgbColor(RgbColor.fromArgbInt(ResUtil.getColor(mContext, color)));
        shapeElement.setCornerRadius(radius);
        return shapeElement;
    }

    public static <E extends Component> E getComponent(Component root, int id) {
        if (root == null) {
            return null;
        }
        try {
            return (E) root.findComponentById(id);
        } catch (ClassCastException ex) {
            LogUtil.error("ComponentUtils", "Could not cast Component to concrete class.");
            throw ex;
        }
    }
}