package com.bakerj.base.router;

import android.content.Context;
import android.net.Uri;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PathReplaceService;

@Route(path = "/replace/replace")
public class PathReplaceServiceImpl implements PathReplaceService {
    private static PathReplaceService pathReplaceService;

    @Override
    public String forString(String path) {
        if (pathReplaceService != null) {
            return pathReplaceService.forString(path);
        }
        return path;
    }

    @Override
    public Uri forUri(Uri uri) {
        if (pathReplaceService != null) {
            return pathReplaceService.forUri(uri);
        }
        return uri;
    }

    @Override
    public void init(Context context) {
        if (pathReplaceService != null) {
            pathReplaceService.init(context);
        }
    }

    public static void setPathReplaceService(PathReplaceService pathReplaceService) {
        PathReplaceServiceImpl.pathReplaceService = pathReplaceService;
    }
}
