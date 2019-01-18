package com.bakerj.base.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;

/**
 * @author BakerJ
 * @date 2017/12/30
 * 降级服务，所有无法识别的路径，降级到H5
 */
@Route(path = "/degrade/degrade")
public class DegradeServiceImpl implements DegradeService {
    private static DegradeService degradeService;

    @Override
    public void onLost(Context context, Postcard postcard) {
//        if (postcard.getUri() != null) {
//            toWeb(postcard.getUri().toString());
//        } else if (postcard.getPath() != null) {
//            toWeb(postcard.getPath());
//        }
        if (degradeService != null) {
            degradeService.onLost(context, postcard);
        }
    }

    @Override
    public void init(Context context) {
        if (degradeService != null) {
            degradeService.init(context);
        }
    }

    public static void setDegradeService(DegradeService degradeService) {
        DegradeServiceImpl.degradeService = degradeService;
    }
}
