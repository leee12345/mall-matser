package com.fo.pp.dao;

import com.alipay.api.AlipayApiException;
import com.fo.pp.pojo.po.AliPayBean;

public interface PayServiceDAO {
    String aliPay(AliPayBean alipayBean) throws AlipayApiException;
}
