package com.kelab.problemcenter.service.Impl;

import com.kelab.info.base.query.BaseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommonService {

    static List<Integer> totalIds(BaseQuery query) {
        List<Integer> ids = new ArrayList<>();
        if (query.getIds() != null) {
            ids.addAll(query.getIds());
        }
        if (query.getId() != null) {
            ids.add(query.getId());
        }
        return ids;
    }
}
