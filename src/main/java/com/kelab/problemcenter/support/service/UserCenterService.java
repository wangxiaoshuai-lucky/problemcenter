package com.kelab.problemcenter.support.service;

import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.problemcenter.support.ParamBuilder;
import com.kelab.problemcenter.support.facade.UserCenterServiceSender;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class UserCenterService {

    private UserCenterServiceSender userCenterServiceSender;

    public UserCenterService(UserCenterServiceSender userCenterServiceSender) {
        this.userCenterServiceSender = userCenterServiceSender;
    }

    public List<UserInfo> queryByUserIds(Context context, List<Integer> ids) {
        List<UserInfo> userInfos = userCenterServiceSender.queryByUserIds(
                ParamBuilder.buildParam(context)
                        .param("ids", Strings.collectionToCommaDelimitedString(ids)));
        if (CollectionUtils.isEmpty(userInfos)) {
            return Collections.emptyList();
        }
        return userInfos;
    }

    public void judgeCallback(Integer userId, boolean ac) {
        userCenterServiceSender.judgeCallback(ParamBuilder.buildParam().param("userId", userId).param("ac", ac));
    }
}
