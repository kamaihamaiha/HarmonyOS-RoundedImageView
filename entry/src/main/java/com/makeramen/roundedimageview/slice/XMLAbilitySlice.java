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
import com.makeramen.roundedimageview.ResourceTable;
import com.makeramen.roundedimageview.utils.Const;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.ShapeElement;

public class XMLAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private static String TAG = XMLAbilitySlice.class.getSimpleName();

    private Button _1_one, _1_two, _1_three, _1_four;

    @Override
    public void onStart(Intent intent)
    {
        super.onStart(intent);
        ComponentContainer rootLayout = (ComponentContainer) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_xmllayout_main, null, false);
        _1_one = ResUtil.findViewById(rootLayout, ResourceTable.Id_test_one);
        _1_two = ResUtil.findViewById(rootLayout, ResourceTable.Id_test_two);
        _1_three = ResUtil.findViewById(rootLayout, ResourceTable.Id_test_three);
        _1_four = ResUtil.findViewById(rootLayout, ResourceTable.Id_test_four);

        setShapeElement(new UiUtil(this));
        setClickListeners();
        setUIContent(rootLayout);
    }

    private void setShapeElement(UiUtil uiUtil) {
        _1_one.setBackground(uiUtil.getShapeElement(ShapeElement.RECTANGLE, ResourceTable.Color_material_lighter_gray, 5.0f));
        _1_two.setBackground(uiUtil.getShapeElement(ShapeElement.RECTANGLE, ResourceTable.Color_material_lighter_gray, 5.0f));
        _1_three.setBackground(uiUtil.getShapeElement(ShapeElement.RECTANGLE, ResourceTable.Color_material_lighter_gray, 5.0f));
        _1_four.setBackground(uiUtil.getShapeElement(ShapeElement.RECTANGLE, ResourceTable.Color_material_lighter_gray, 5.0f));
    }

    private void setClickListeners() {
        _1_one.setClickedListener(this);
        _1_two.setClickedListener(this);
        _1_three.setClickedListener(this);
        _1_four.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        Intent intent;

        switch (component.getId()) {
            case ResourceTable.Id_test_one:
                LogUtil.info(TAG, "Id_basicNoTitle");
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                intent.setParam("testcase", Const.TEST_CASE_1);
                present(new CircleXmlImageviewSlice(), intent);
                break;
            case ResourceTable.Id_test_two:
                LogUtil.info(TAG, "Id_basic");
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                intent.setParam("testcase", Const.TEST_CASE_2);
                present(new CircleXmlImageviewSlice(), intent);
                break;
            case ResourceTable.Id_test_three:
                LogUtil.info(TAG, "Id_basicLongContent");
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                intent.setParam("testcase", Const.TEST_CASE_3);
                present(new CircleXmlImageviewSlice(), intent);
                break;
            case ResourceTable.Id_test_four:
                intent = new Intent();
                intent.addFlags(Intent.FLAG_ABILITY_NEW_MISSION);
                intent.setParam("testcase", Const.TEST_CASE_4);
                present(new CircleXmlImageviewSlice(), intent);
                break;
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}