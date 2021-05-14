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
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

public class TestAbilitySlice extends AbilitySlice {
    private static String TAG = TestAbilitySlice.class.getSimpleName();

    private Button mainButton, xmlButton;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        LogUtil.info(TAG, "onStart() of Otto1");

        ComponentContainer rootLayout = (ComponentContainer) LayoutScatter.getInstance(this)
                .parse(ResourceTable.Layout_ability_test, null, false);

        mainButton = (Button) rootLayout.findComponentById(ResourceTable.Id_mainButton);
        xmlButton = (Button) rootLayout.findComponentById(ResourceTable.Id_xmlButton);

        mainButton.setClickedListener(component -> present(new MainAbilitySlice(), new Intent()));
        xmlButton.setClickedListener(component -> present(new XMLAbilitySlice(), new Intent()));

        super.setUIContent(rootLayout);
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
