package com.tang_tzb.sorm.core;

import java.sql.ResultSet;

public interface CallBack {
    public Object doExecute(ResultSet rs);
}
