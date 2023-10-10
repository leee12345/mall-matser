package com.fo.pp.service;

import com.alipay.api.AlipayApiException;
import com.fo.pp.dao.PayServiceDAO;
import com.fo.pp.pojo.po.AliPayBean;
import com.fo.pp.pojo.po.Alipay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService implements PayServiceDAO {
    private static final Logger logger = LoggerFactory.getLogger(PayService.class);

    @Autowired
    private Alipay alipay;

    public String aliPay(AliPayBean aliPayBean) throws AlipayApiException {
        logger.info("调用支付服务接口");
        return alipay.pay(aliPayBean);
    }
}
